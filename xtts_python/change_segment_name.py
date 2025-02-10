import os

def rename_files(folder_path):
    # 변경을 원하는 접두어(prefix) 문자열
    prefix = "[인공지능] 2. History of AI, Intelligent Agents_A반_"

    # 폴더 내 모든 파일명을 순회합니다.
    for file_name in os.listdir(folder_path):
        # .wav 파일에만 적용
        if file_name.endswith(".wav"):
            # 파일명이 prefix로 시작한다면
            if file_name.startswith(prefix):
                # prefix 길이만큼 잘라낸 나머지를 새로운 파일명으로 사용
                new_name = file_name[len(prefix):]

                old_path = os.path.join(folder_path, file_name)
                new_path = os.path.join(folder_path, new_name)

                # 파일명 변경
                os.rename(old_path, new_path)
                print(f"Renamed: {old_path} --> {new_path}")

if __name__ == "__main__":
    folder_path = "/dev/hdd/user/kjy/ict/datasets/preprocess/[인공지능] 2. History of AI, Intelligent Agents_A반"
    rename_files(folder_path)