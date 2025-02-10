from TTS.api import TTS

def text_to_tts(model_path, file_path, text):

    # 사용자 학습한 모델 경로로 TTS 객체 초기화
    tts = TTS(
        model_path=model_path,
        config_path=model_path + "/config.json",
        gpu=True
    )

    speaker_wav = model_path + "/speaker.wav"

    # 음성 합성
    tts.tts_to_file(
        text=text,
        file_path=file_path,
        speaker_wav=[speaker_wav],
        language="ko",
        split_sentences=True
    )

text = """안녕하세요, 유애나! 이렇게 만나서 너무 반가워요. 오늘 하루는 어떻게 보내고 계신가요? 따뜻한 이야기 나눌 수 있으면 좋겠어요.

안녕하세요! 이렇게 만나서 정말 반가워요. 오늘 하루는 어떻게 보내셨나요? 궁금한 이야기나 나누고 싶으신 것들이 있다면 언제든지 말씀해 주세요

이렇게 이야기 나눌 수 있어서 정말 기쁘네요. 오늘 하루는 어땠나요? 어떤 이야기를 나눌까요?

오늘 하루도 따뜻하고 행복한 일들로 가득하길 바랄게요. 어떤 이야기를 나눌까요?

그럼, 시작해볼까요? 여러분의 마음속에 따뜻한 감정이 가득하길 바라요. 함께 해요!

혹시 오늘 어떤 이야기 나눌까요? 저는 여러분과 소통하는 걸 정말 좋아해요."""
text_to_tts("run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_GH-January-14-2025_08+11PM-0000000", "output.wav", text)