package com.akkarimzai.exceptions

class NotFoundException(key: String, value: Any) : Exception("{$key}: ($value) not found")