
local platformInterface=require("app.components.platform.PlatformInterface")


if device.platform == "ios" then
    require("app.components.platform.PlatformIOSImpl")
elseif device.platform == "android" then
    require("app.components.platform.PlatformAndroidImpl")
else --默认ios平台实现
    require("app.components.platform.PlatformIOSImpl")
end



return platformInterface
