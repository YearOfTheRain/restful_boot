Restful Api写法
    路径定义
        域名无法区分出是api还是页面功能的时候，api路径后面统一加/api用于区分是接口服务
    词性使用
        定义自定义路径部分时，使用名词的复数形式定义一个资源，如若有动词词性在url中考虑以下划线区分。
    基本操作
        GET /users                    # 获取用户列表
        GET /users/{userId}       # 查看某个具体的用户信息
        POST /users                 # 新建一个用户
        PUT /users/{userId}       # 全量更新某一个用户信息
        PATCH /users/{userId}   # 选择性更新某一个用户信息
        DELETE /users/{userId} # 删除某一个用户
    批量操作
        POST /users/_mget         # 批量获取多个用户
        POST /users/_mcreate    # 批量创建多个用户
        POST /users/_mupdate   # 批量更新多个用户
        POST /users/_mdelete    # 批量删除多个用户
        POST /users/_bulk          # 批量功能组装
    动词词性加入url（原则上此种情况是不被推荐的）
        GET /users/_search        # 搜索用户
        POST /users/_init         # 初化所有用户
