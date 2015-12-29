
--[[
    具体平台方法总接口 这里仅作申明即可 具体实现在对应的平台文件里面处理
    
    外部调用示例
    local platformHelper=require("app.components.platform.PlatformHelper")
    platformHelper.xxx(xxx)
    注意不要引用本文件 引用PlatformHelper即可
    
    
    向系统剪贴板发送字符串
    PlatformInterface.setStringToPasteboard(params)
        params 请求参数 {str="xx"}
        ------
        返回true表示成功
        
--]]

local PlatformInterface={}



function PlatformInterface.setStringToPasteboard(params)
    print("error: toImpl platformXXXImpl files")
end

function PlatformInterface.getUsedMemory()
    print("error: toImpl platformXXXImpl files")
end

function PlatformInterface.getAvailableMemory()
    print("error: toImpl platformXXXImpl files")
end

function PlatformInterface.getMoreInfo()
    print("error: toImpl platformXXXImpl files")
end

--本地推送相关
--启用本地推送功能 提示用户授权[第一次运行]
function PlatformInterface.enabledLPNS()
    print("error: toImpl platformXXXImpl files")
end
--清理掉本游戏向系统注册的所有推送 一般也可以在游戏启动后执行在ios上面不执行也可以 会被游戏接收掉
function PlatformInterface.cleanAllLocalNotifications()
    print("error: toImpl platformXXXImpl files")
end
--去除游戏图标上面的数字
function PlatformInterface.cleanIconBadgeNumber()
    print("error: toImpl platformXXXImpl files")
end
--向系统添加一个本地推送通知
function PlatformInterface.pushLocalNotification(arg)
    print("error: toImpl platformXXXImpl files")
end
--清理掉某个本地推送
function PlatformInterface.cancelNotification(uKey)
    print("error: toImpl platformXXXImpl files")
end
--获取所有本地推送信息
function PlatformInterface.getLocalNotification()
    print("error: toImpl platformXXXImpl files")
end
--获取有多少本地推送
function PlatformInterface.getLocalNotificationNum()
    print("error: toImpl platformXXXImpl files")
end




return PlatformInterface
