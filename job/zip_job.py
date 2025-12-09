import os 

file_names = ['a', 'b', 'c', 'd']
FILE_PATH = os.environ.get('FILES_PATH', './files')
ZIP_PATH = os.environ.get('ZIP_PATH', './zipped')
VERSION = os.getenv("VERSION")

def create_files():
    for file in file_names:
        try:
            if not os.path.exists(FILE_PATH):
                os.makedirs(FILE_PATH)
            with open(os.path.join(FILE_PATH, f'{file}.txt'), 'w') as f:
                pass
        except FileNotFoundError:
            print(f"File {file}.txt not found in {FILE_PATH}")

def zip_files():
    for file in file_names:
        try:
            if not os.path.exists(ZIP_PATH):
                os.makedirs(ZIP_PATH)
            new_zip_name = os.path.join(ZIP_PATH, f'{file}_{VERSION}.zip')
            full_file_path = os.path.join(FILE_PATH, f'{file}.txt')
            command = f"zip {new_zip_name} {full_file_path}"
            os.system(command)
            print(f"Zipped {file}.txt to {file}_{VERSION}.zip")
        except Exception as e:
            print(f"Error zipping {file}.txt: {e}")
            
if __name__ == "__main__":
    create_files()
    zip_files()
    