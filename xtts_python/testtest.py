# from TTS.api import TTS
# tts = TTS("tts_models/multilingual/multi-dataset/xtts_v2", gpu=True)

# # generate speech by cloning a voice using default settings
# tts.tts_to_file(text="세상에서 가장 어려운 일은 사람이 사람의 마음을 얻는 일이야. 내가 좋아하는 사람이 나를 좋아해 주는 건 기적이야.",
#                 file_path="output.wav",
#                 speaker_wav = [f"run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_GH-January-14-2025_02+25PM-0000000/output187_join.wav"],
#                 language="ko",
#                 split_sentences=True
#                 )




# from TTS.api import TTS

# # 사용자 학습한 모델 경로로 TTS 객체 초기화
# tts = TTS(
#     model_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_JY-January-08-2025_09+08PM-0000000",
#     config_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_JY-January-08-2025_09+08PM-0000000/config.json",
#     gpu=True
# )

# # 음성 합성
# tts.tts_to_file(
#     text="안녕하세요! 반갑습니다. 오늘은 어떤 주제에 대해 이야기해 볼까요? 인공지능에 관한 질문이든, 다른 주제든지 자유롭게 말씀해 주세요. Let's have a productive discussion!",
#     file_path="output.wav",
#     speaker_wav=[f"datasets/preprocess/[인공지능] 1. Intro_to_AI(인공지능소개)_A반/[인공지능] 1. Intro_to_AI(인공지능소개)_A반_segment_2651.wav"],
#     language="ko",
#     split_sentences=True
# )




# from TTS.api import TTS

# # 사용자 학습한 모델 경로로 TTS 객체 초기화
# tts = TTS(
#     model_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_GH-January-14-2025_02+25PM-0000000",
#     config_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_GH-January-14-2025_02+25PM-0000000/config.json",
#     gpu=True
# )

# # 음성 합성
# tts.tts_to_file(
#     text="세상에서 가장 어려운 일은 사람이 사람의 마음을 얻는 일이야. 내가 좋아하는 사람이 나를 좋아해 주는 건 기적이야.",
#     file_path="output.wav",
#     speaker_wav=[f"run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_GH-January-14-2025_02+25PM-0000000/output187_join.wav"],
#     language="ko",
#     split_sentences=True
# )


# from TTS.api import TTS

# # 사용자 학습한 모델 경로로 TTS 객체 초기화
# tts = TTS(
#     model_path="run/training/backup/GPT_XTTS_v2.0_BBANGHYONG_FT_SY-January-06-2025_03+04PM-0000000",
#     config_path="run/training/backup/GPT_XTTS_v2.0_BBANGHYONG_FT_SY-January-06-2025_03+04PM-0000000/config.json",
#     gpu=True
# )

# # 음성 합성
# tts.tts_to_file(
#     text="안녕! 뭐해? 빨리 얘기해봐, 시간 없어! 방가 방가! 뭐하냐 임마? 오늘도 열심히 살아봐!",
#     file_path="output.wav",
#     speaker_wav=[f"run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_SY-January-14-2025_02+28PM-0000000/2_join.wav"],
#     language="ko",
#     split_sentences=True
# )



# from TTS.api import TTS

# # 사용자 학습한 모델 경로로 TTS 객체 초기화
# tts = TTS(
#     model_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_JH3-January-14-2025_02+25PM-0000000",
#     config_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_JH3-January-14-2025_02+25PM-0000000/config.json",
#     gpu=True
# )

# # 음성 합성
# tts.tts_to_file(
#     text="세상에서 가장 어려운 일은 사람이 사람의 마음을 얻는 일이야. 내가 좋아하는 사람이 나를 좋아해 주는 건 기적이야.",
#     file_path="output.wav",
#     speaker_wav=[f"run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_JH3-January-14-2025_02+25PM-0000000/12_join.wav"],
#     language="ko",
#     split_sentences=True
# )


from TTS.api import TTS

# 사용자 학습한 모델 경로로 TTS 객체 초기화
tts = TTS(
    model_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_DNI-January-14-2025_02+24PM-0000000",
    config_path="run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_DNI-January-14-2025_02+24PM-0000000/config.json",
    gpu=True
)

# 음성 합성
tts.tts_to_file(
    text="세상에서 가장 어려운 일은 사람이 사람의 마음을 얻는 일이야. 내가 좋아하는 사람이 나를 좋아해 주는 건 기적이야.",
    file_path="output.wav",
    speaker_wav=[f"run/training/GPT_XTTS_v2.0_BBANGHYONG_FT_DNI-January-14-2025_02+24PM-0000000/5_join.wav"],
    language="ko",
    split_sentences=True
)
