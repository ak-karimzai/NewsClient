package com.akkarimzai.exceptions

class NotFoundException(key: String, value: Any) : BaseException("{$key}: ($value) not found")