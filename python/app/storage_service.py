from firebase_admin import credentials, initialize_app, storage
from typing import BinaryIO, List
import os


class StorageService:
    def __init__(self):
        # Get the path to the service account key file
        service_account_key_path = os.path.join(
            os.path.dirname(__file__), "serviceAccountKey.json"
        )

        # Initialize Firebase
        cred = credentials.Certificate(service_account_key_path)
        initialize_app(cred, {"storageBucket": "yy-f284e.appspot.com"})
        self.client = storage.bucket()

    def upload_video(self, post_id: str, role: str, file: BinaryIO) -> str:
        destination_blob_name = f"videos/{post_id}/{file.filename}_{role}"
        blob = self.client.blob(destination_blob_name)
        blob.upload_from_file(file, content_type="video/mp4")
        blob.make_public()
        return blob.public_url

    def get_videos_in_folder(self, post_id: str) -> List[str]:
        folder_path = f"videos/{post_id}/"
        blobs = self.client.list_blobs(prefix=folder_path)
        video_urls = [blob.public_url for blob in blobs]
        return video_urls

    def delete_files_in_folder(self, post_id: str):
        folder_path = f"videos/{post_id}/"
        blobs = self.client.list_blobs(prefix=folder_path)
        for blob in blobs:
            blob.delete()
