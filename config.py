import os
from dotenv import load_dotenv
load_dotenv('.env')

class DBEnvs:
    HOST = os.getenv('MYSQL_HOST')
    DB_NAME = os.getenv('MYSQL_DB_NAME')
    USERNAME = os.getenv('MYSQL_USERNAME')
    PASSWORD = os.getenv('MYSQL_PASSWORD')
    
    
class JWTEnvs:
    SECRET_KEY = os.getenv('SECRET_KEY')
    ALGORITHM = os.getenv('ALGORITHM')
    ACCESS_TOKEN_EXPIRE_MINUTES = int(os.getenv('ACCESS_TOKEN_EXPIRE_MINUTES'))