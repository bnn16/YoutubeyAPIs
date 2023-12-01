from fastapi import APIRouter, File, UploadFile, Form
from app.firebase_utils import (
    upload_video,
    get_videos_in_folder,
    delete_files_in_folder,
)

router = APIRouter()


@router.post("/upload/{post_id}")
async def upload_video_route(file: UploadFile, post_id: str, role: str = Form(...)):
    return upload_video(file, post_id, role)


@router.get("/upload/{post_id}")
def get_videos_in_folder_route(post_id: str):
    return get_videos_in_folder(post_id)


@router.delete("/upload/{post_id}")
def delete_files_in_folder_route(post_id: str):
    return delete_files_in_folder(post_id)
