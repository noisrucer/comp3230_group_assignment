from typing import Union
from enum import Enum

from fastapi import HTTPException, status
from pydantic import BaseModel
    
# Users
class UserBase(BaseModel):
    email: str
        
    
class UserCreate(UserBase):
    password: str
    
    
class UserCreateOut(UserBase):
    user_id: int
    
    class Config:
        orm_mode = True
    
class User(UserBase):
    class Config:
        orm_mode = True
    
# JWT
class Token(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str

class TokenData(BaseModel):
    username: Union[str, None] = None