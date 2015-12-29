--Android Impl PlatformFunction

local luaj = require("cocos.cocos2d.luaj")
local PlatformInterface=require("app.components.platform.PlatformInterface")


function PlatformInterface.setStringToPasteboard(params)
    local content=params.str

    local className = "org/cocos2dx/lua/AppActivity"
    local ok,code = luaj.callStaticMethod(className,"setStringToSysClipboard", { content }, "(Ljava/lang/String;)V")
    if not ok then
        print("luaj error:", code)
        return false
    end
    return true
end

function PlatformInterface.getUsedMemory()
    local className = "org/cocos2dx/lua/AppActivity"
    local ok,ret = luaj.callStaticMethod(className,"getUsedMemory", nil, "()F")
    return ret
end

function PlatformInterface.getAvailableMemory()
    local className = "org/cocos2dx/lua/AppActivity"
    local ok,ret = luaj.callStaticMethod(className,"getAvailableMemory", nil, "()F")
    return ret
end

function PlatformInterface.getMoreInfo()
    local className = "org/cocos2dx/lua/AppActivity"
    local ok,ret = luaj.callStaticMethod(className,"getMoreMemoryInfo", nil, "()Ljava/lang/String;")
    return ret
end

--清理掉本游戏向系统注册的所有推送 一般也可以在游戏启动后执行在ios上面不执行也可以 会被游戏接收掉
function PlatformInterface.cleanAllLocalNotifications()
    local className = "org/cocos2dx/lua/AppActivity"
    luaj.callStaticMethod(className,"clearAllNotices");
end
--向系统添加一个本地推送通知
function PlatformInterface.pushLocalNotification(arg)

    local ta = {}
    for k, v in pairs(arg) do
        if k ~= "badgeNum" then
            ta[#ta +1] = k
        end
    end
    table.sort(ta)
    local tb ={}
    for k, v in pairs(ta) do
        tb[#tb + 1] = arg[v]
    end

    local className = "org/cocos2dx/lua/AppActivity"
    local sig = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)Z"
    local ok,ret = luaj.callStaticMethod(className,"pushLocalNotification", tb,sig)
    if ok then
        return ret
    end

end
--清理掉某个本地推送
function PlatformInterface.cancelNotification(uKey)
    print(type(uKey))
    local className = "org/cocos2dx/lua/AppActivity"
    local ok, ret = luaj.callStaticMethod(className,"clearNoticeByKey",{uKey},"(Ljava/lang/String;)Z")
    if ok then
        return ret
    end
end
--获取所有本地推送信息
function PlatformInterface.getLocalNotification()
    local className = "org/cocos2dx/lua/AppActivity"
    local ok, ret = luaj.callStaticMethod(className,"getAllNotices", nil, "()Ljava/lang/String;")

    if ok then
        local code, tab = pcall(loadstring(string.format('do local _=%s return _ end',ret)));
        if code then
            return tab
        else
            return nil
        end
    else
        return nil
    end
end
--获取有多少本地推送
function PlatformInterface.getLocalNotificationNum()
    local className = "org/cocos2dx/lua/AppActivity"
    local ok,ret = luaj.callStaticMethod(className,"getLocalNotificationNum", nil, "()I")
    if ok then
        return ret
    end
end
