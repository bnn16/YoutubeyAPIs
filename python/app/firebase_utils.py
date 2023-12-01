from firebase_admin import credentials, initialize_app, storage
from fastapi import HTTPException
import os

# Get the path to the service account key file
service_account_key_path = os.path.join(os.path.dirname(__file__), "serviceAccountKey.json")

# Initialize Firebase
cred = credentials.Certificate(service_account_key_path)

initialize_app(cred, {"storageBucket": "yy-f284e.appspot.com"})


def upload_video(file, post_id, role):
    if file:
        try:
            # Get the file's binary content
            file_contents = file.file.read()

            # Create a storage client
            client = storage.bucket()

            # Define the path to store the file in Firebase Storage
            destination_blob_name = f"videos/{post_id}/{file.filename}_{role}"

            blob = client.blob(destination_blob_name)
            blob.upload_from_string(file_contents, content_type="video/mp4")
            blob.make_public()

            public_url = blob.public_url

            return {"file_url": public_url}
        except Exception as e:
            raise HTTPException(status_code=500, detail=str(e))
    return {"error": "No video file provided"}


def get_videos_in_folder(post_id):
    try:
        client = storage.bucket()

        folder_path = f"videos/{post_id}/"

        blobs = client.list_blobs(prefix=folder_path)

        video_urls = [blob.public_url for blob in blobs]

        return {"videos": video_urls}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


def delete_files_in_folder(post_id):
    try:
        client = storage.bucket()

        folder_path = f"videos/{post_id}/"

        blobs = client.list_blobs(prefix=folder_path)

        for blob in blobs:
            blob.delete()

        return {
            "message": f"All files in the folder '{folder_path}' have been deleted."
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
