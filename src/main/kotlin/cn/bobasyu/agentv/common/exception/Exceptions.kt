package cn.bobasyu.agentv.common.exception

import cn.bobasyu.agentv.common.http.ResultCode

/**
 * 基础异常封装，仅添加了错误码信息
 */
open class BaseException(
    val code: Int = ResultCode.INTERNAL_ERROR.code,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 数据库为查询到数据异常封装
 */
class NoSuchRecordInDatabaseException(
    message: String = "",
    cause: Throwable? = null
) : BaseException(ResultCode.INTERNAL_ERROR.code, "No such record in database.$message", cause)

class UnauthorizedException(
    message: String = "",
    cause: Throwable? = null
) : BaseException(ResultCode.UNAUTHORIZED.code, "Unauthorized.$message", cause)

class CommonBaseException(
    message: String,
    cause: Throwable? = null
) : BaseException(code = 500, message = message, cause = cause)