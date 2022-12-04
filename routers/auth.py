import random
from random import randbytes
import string
import secrets
from datetime import datetime, timedelta
import hashlib

from fastapi import APIRouter, status, HTTPException, Depends, Request
from fastapi.responses import JSONResponse
from fastapi_jwt_auth import AuthJWT
from fastapi_jwt_auth.exceptions import AuthJWTException
from pydantic import BaseModel
from email_validator import validate_email, EmailNotValidError
from sqlalchemy.orm import Session 
from fastapi_mail import ConnectionConfig, FastMail, MessageSchema, MessageType
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jose import jwt, JWTError

from database import get_db
import models, schemas
from utils import hash_password, verify_password
from config import JWTEnvs
from oauth2 import create_access_token, decode_jwt

oauth2_scheme = OAuth2PasswordBearer(
    tokenUrl="/auth/login",
    scheme_name="JWT",
    )

router = APIRouter(
    prefix="/auth",
    tags=["auth"]
)

    
@router.post("/register", status_code=status.HTTP_201_CREATED, response_model=schemas.UserCreateOut)
async def register(user: schemas.UserCreate, db: Session = Depends(get_db)):
    user_dict = user.dict()
    
    # Check if there's a duplicated email in the DB
    dup_email_entry = db.query(models.User).filter(models.User.email == user_dict['email']).first()
    if dup_email_entry:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Email already existed"
        )
    
    # Hash password
    hashed_password = hash_password(user_dict['password'])
    user_dict.update(
        password=hashed_password
    )
    
    # Save to DB
    new_user = models.User(**user_dict)
    db.add(new_user) 
    db.commit()
    db.refresh(new_user)
    return new_user
    
def authenticate_user(email: str, password: str, db: Session = Depends(get_db)):

    user = db.query(models.User).filter(models.User.email == email).first()
    if not user:
        return False
    if not verify_password(password, user.password):
        return False
    return user


def get_user_by_email(email: str, db: Session = Depends(get_db)):
    user = db.query(models.User).filter(models.User.email == email).first()
    if not user:
        return False
    return user
    
@router.post('/login', response_model=schemas.Token)
async def login(
    form_data: OAuth2PasswordRequestForm = Depends(),
    Authorize: AuthJWT = Depends(),
    db: Session = Depends(get_db)
    ):
    user = authenticate_user(form_data.username, form_data.password, db)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"}
        )
    access_token_expires = timedelta(minutes=JWTEnvs.ACCESS_TOKEN_EXPIRE_MINUTES)
    refresh_token_expires = timedelta(minutes=3)
    access_token = Authorize.create_access_token(subject=user.email, expires_time=access_token_expires)
    refresh_token = Authorize.create_refresh_token(subject=user.email, expires_time=refresh_token_expires)
    return {"access_token": access_token, "refresh_token": refresh_token, "token_type": "bearer"}

@router.post('/refresh')
def refresh(Authorize: AuthJWT = Depends()):
    """
    The jwt_refresh_token_required() function insures a valid refresh
    token is present in the request before running any code below that function.
    we can use the get_jwt_subject() function to get the subject of the refresh
    token, and use the create_access_token() function again to make a new access token
    """
    Authorize.jwt_refresh_token_required()
    print("AA")

    current_user = Authorize.get_jwt_subject()
    access_token_expires = timedelta(minutes=JWTEnvs.ACCESS_TOKEN_EXPIRE_MINUTES)
    new_access_token = Authorize.create_access_token(subject=current_user, expires_time=access_token_expires)
    return {"access_token": new_access_token}

@router.get('/protected')
def protected(Authorize: AuthJWT = Depends()):
    Authorize.jwt_required()

    current_user = Authorize.get_jwt_subject()
    return {"user": current_user}