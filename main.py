from fastapi import FastAPI
from fastapi import APIRouter, status, HTTPException, Depends, Request
from fastapi.responses import JSONResponse
from fastapi_jwt_auth import AuthJWT
from fastapi_jwt_auth.exceptions import AuthJWTException
from pydantic import BaseModel

from database import engine
import models, schemas
from routers import auth

models.Base.metadata.create_all(bind=engine)


app = FastAPI()

class Settings(BaseModel):
    authjwt_secret_key: str = "secret"
    
@AuthJWT.load_config
def get_config():
    return Settings()

@app.exception_handler(AuthJWTException)
def authjwt_exception_handler(request: Request, exc: AuthJWTException):
    return JSONResponse(
        status_code=exc.status_code,
        content={"detail": exc.message}
    )
    
app.include_router(auth.router)

@app.get("/")
async def root():
    return {"message": "hello to the root page"}