from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts

# 설정 로드
config = XttsConfig()
config.load_json("run/training/jh/GPT_XTTS_v2.0_BBANGHYONG_FT-January-02-2025_03+25PM-0000000/config.json")  # config.json 파일 경로

# 모델 초기화 및 가중치 로드
model = Xtts.init_from_config(config)
model.load_checkpoint(config, "ex", eval=True)
model.cuda()

import torch
import torchaudio

# 화자 음성 샘플로부터 임베딩 추출
gpt_cond_latent, speaker_embedding = model.get_conditioning_latents(audio_path=["dataset_jh/contents/wavs/88.wav"])  # 화자 음성 파일 경로

# 텍스트를 음성으로 변환
output = model.inference(
    text="안녕, 오랜만이야야",
    language="ko",  # 언어 코드 (예: 한국어는 'ko')
    gpt_cond_latent=gpt_cond_latent,
    speaker_embedding=speaker_embedding
)

# 결과 저장
torchaudio.save("output.wav", torch.tensor(output["wav"]).unsqueeze(0), 24000)
