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

	-- 安卓通知信息只需要 key time title content sound 
	-- 表中的顺序不能变
    local t = {arg.time,arg.key,arg.title,arg.content,arg.sound or "NoSound"} 
    local className = "org/cocos2dx/lua/AppActivity"
    local sig = "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z"
    local ok,ret = luaj.callStaticMethod(className,"pushLocalNotification", t,sig)
    if ok then
        return ret
    end

end
--清理掉某个本地推送
function PlatformInterface.cancelNotification(uKey)
    local className = "org/cocos2dx/lua/AppActivity"
    local ok, ret = luaj.callStaticMethod(className,"clearNoticeByKey",{uKey},"(Ljava/lang/String;)I")
    if ok then
        return ret
    end
end
--获取所有本地推送信息
function PlatformInterface.getLocalNotification()
    local className = "org/cocos2dx/lua/AppActivity"
    -- 返回一个字符串，符合 table 的数据格式
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