import os
import torch
import torchaudio
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts

print("Loading model...")
config = XttsConfig()
config.load_json("run/training/jh/GPT_XTTS_v2.0_BBANGHYONG_FT-January-02-2025_03+25PM-0000000/config.json")
model = Xtts.init_from_config(config)
model.load_checkpoint(config, checkpoint_dir="ex", use_deepspeed=True)
model.cuda()

print("Computing speaker latents...")
gpt_cond_latent, speaker_embedding = model.get_conditioning_latents(audio_path=["/dev/hdd/user/kjy/ict/dataset_jh/contents/wavs/88.wav"])

print("Inference...")
out = model.inference(
    "It took me quite a long time to develop a voice and now that I have it I am not going to be silent.",
    "en",
    gpt_cond_latent,
    speaker_embedding,
    temperature=0.7, # Add custom parameters here
)
torchaudio.save("xtts.wav", torch.tensor(out["wav"]).unsqueeze(0), 24000)