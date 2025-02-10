from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts

config = XttsConfig()
config.load_json("/dev/hdd/user/kjy/ict/run/training/GPT_XTTS_v2.0_BBANGHYONG_FT-January-02-2025_02+01PM-0000000/config.json")
model = Xtts.init_from_config(config)
model.load_checkpoint(config, checkpoint_dir="/dev/hdd/user/kjy/ict/run/training/GPT_XTTS_v2.0_BBANGHYONG_FT-January-02-2025_02+01PM-0000000/", eval=True)
model.cuda()

outputs = model.synthesize(
    "살려줘",
    config,
    speaker_wav="/dev/hdd/user/kjy/ict/datasets/[인공지능] 2. History of AI, Intelligent Agents_A반.wav",
    gpt_cond_len=3,
    language="ko",
)
