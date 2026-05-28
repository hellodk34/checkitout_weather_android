package com.checkitout.weather.data

object AuthConstants {
    /*
    以下内容请自行修改，在和风天气控制台 https://console.qweather.com/ 注册账号 - 创建项目（获取项目 id） - 进入项目创建凭据（获取凭据 id）
    其中凭据建议使用 JWT（JSON Web Token） 认证，使用如下命令创建
    openssl genpkey -algorithm ED25519 -out ed25519-private.pem
    openssl pkey -pubout -in ed25519-private.pem > ed25519-public.pem
    将 ed25519-public.pem 内容粘贴到创建凭据的页面
    将 ed25519-private.pem 的第二行内容粘贴到下方 PRIVATE_KEY_PEM 变量中
     */
    
    const val HOST = "填入你的 HOST"
    const val PROJECT_ID = "填入你的 project id"
    const val CREDENTIAL_ID = "填入你的 credential id"
    const val PRIVATE_KEY_PEM = "填入你的私钥字符串，不需要第一行 -----BEGIN PRIVATE KEY----- ，也不需要第二行 -----END PRIVATE KEY----- ，只需复制粘贴 pem 文件第二行内容到这里即可"
    const val API_BASE_URL = "https://$HOST/"
}
