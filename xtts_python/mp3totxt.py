import os
import argparse
import whisper
from pydub import AudioSegment

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mp3_path", type=str, required=True, help="MP3 파일 경로")
    parser.add_argument("--output_dir", type=str, required=True, help="WAV 세그먼트와 메타데이터를 저장할 폴더 경로")
    parser.add_argument("--device", type=str, default="cuda:0", help="사용할 디바이스 (예: 'cuda:0' 또는 'cpu')")
    args = parser.parse_args()

    mp3_path = args.mp3_path
    output_dir = args.output_dir
    device_str = args.device

    # MP3 파일 베이스 이름 (확장자 제외)
    base_name = os.path.splitext(os.path.basename(mp3_path))[0]

    # 출력 폴더 생성 (MP3 파일 이름 기반 서브 폴더)
    output_subdir = os.path.join(output_dir, base_name)
    os.makedirs(output_subdir, exist_ok=True)

    # 메타데이터 파일 이름 (mp3 파일명 기반)
    metadata_filename = f"{base_name}_metadata.txt"
    metadata_path = os.path.join(output_subdir, metadata_filename)

    # 기존 메타데이터 파일 있으면 제거 (초기화)
    if os.path.exists(metadata_path):
        os.remove(metadata_path)

    # Whisper 모델 로드
    print(f"[INFO] Using device: {device_str}")
    model = whisper.load_model("large", device=device_str)  # 필요시 "small", "medium" 등 사용 가능

    # MP3 파일 처리
    print(f"Processing file: {mp3_path}")
    result = model.transcribe(mp3_path, verbose=True, language="ko")
    segments = result["segments"]

    # MP3를 pydub으로 불러오기
    audio = AudioSegment.from_file(mp3_path, format="mp3")

    file_number = 1
    with open(metadata_path, "w", encoding="utf-8") as f_meta:
        for seg in segments:
            start_time = seg["start"]  # 시작 (초)
            end_time = seg["end"]      # 종료 (초)
            text = seg["text"]         # 추출된 자막 텍스트

            # 밀리초 단위 변환
            start_ms = int(start_time * 1000)
            end_ms = int(end_time * 1000)

            # 구간 오디오 추출
            segment_audio = audio[start_ms:end_ms]

            # 세그먼트 wav 파일 이름 (mp3 베이스이름 + segment_0001.wav 등)
            out_filename = f"{base_name}_segment_{file_number:04d}.wav"
            out_path = os.path.join(output_subdir, out_filename)

            # WAV 내보내기
            segment_audio.export(out_path, format="wav")

            # 메타데이터 작성: "파일명.wav|자막"
            line = f"{out_filename}|{text.strip()}|{text.strip()}\n"
            f_meta.write(line)

            file_number += 1

    print("모든 처리가 완료되었습니다!")
    print(f"총 {file_number - 1}개의 세그먼트 생성")
    print(f"메타데이터: '{metadata_path}'")

if __name__ == "__main__":
    main()
