--IOS Impl PlatformFunction

local luaoc = require("cocos.cocos2d.luaoc")
local PlatformInterface=require("app.components.platform.PlatformInterface")


function PlatformInterface.setStringToPasteboard(params)
    local content=params.str
    
    local ok=luaoc.callStaticMethod("IosPlatformImpl","setStringToPasteboard",{content=content})
    if not ok then
        return false
    end
    return true
end


function PlatformInterface.getUsedMemory()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","getUsedMemory",nil)
    return ret
end

function PlatformInterface.getAvailableMemory()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","getAvailableMemory",nil)
    return ret
end

function PlatformInterface.getMoreInfo()
    return ""
end

--IOS本地推送具体实现
function PlatformInterface.enabledLPNS()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","enabledLPNS",nil)
    return ret
end
--清理掉本游戏向系统注册的所有推送 一般也可以在游戏启动后执行在ios上面不执行也可以 会被游戏接收掉
function PlatformInterface.cleanAllLocalNotifications()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","cleanAllLocalNotifications",nil)
    return ret
end
--去除游戏图标上面的数字
function PlatformInterface.cleanIconBadgeNumber()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","cleanIconBadgeNumber",nil)
    return ret
end
--向系统添加一个本地推送通知
function PlatformInterface.pushLocalNotification(arg)
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","pushLocalNotification",arg)
    return ret
end
--清理掉某个本地推送
function PlatformInterface.cancelNotification(uKey)
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","cancelNotification",{ukey=uKey})
    return ret
end
--获取所有本地推送信息
function PlatformInterface.getLocalNotification()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","getLocalNotification",nil)
    return ret
end
--获取有多少本地推送
function PlatformInterface.getLocalNotificationNum()
    local ok,ret=luaoc.callStaticMethod("IosPlatformImpl","getLocalNotificationNum",nil)
    return ret
end

