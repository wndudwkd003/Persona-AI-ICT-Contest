import os

# 폴더 경로 설정
folder_path = "/dev/hdd/user/kjy/ict/dataset_gh/datasets/wavs"

# 폴더 내 파일 이름 변경
for file_name in os.listdir(folder_path):
    if file_name.startswith("output") and file_name.endswith(".wav"):
        # 새 파일 이름 생성
        new_name = file_name.replace("output", "")
        old_path = os.path.join(folder_path, file_name)
        new_path = os.path.join(folder_path, new_name)
        
        # 파일 이름 변경
        os.rename(old_path, new_path)
        print(f"변경 완료: {file_name} -> {new_name}")

print("모든 파일 이름 변경 완료!")
