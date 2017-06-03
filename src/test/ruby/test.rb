require_relative 'my_http'
require_relative 'my_zip'
require 'solid_assert'

target = 'http://localhost:8080'
getPath = '/a/plugins/gerrit-support/collect'
res = doQuery(target+getPath, {
    "gerritVersion" => "true",
    "memInfo" => "true",
    "cpuInfo" => "true",
    "diskInfo" => "true",
    "configInfo" => "true",
    "pluginsInfo" => "true",
})
assert res.code == "201"
location = res.header['location']

res = doGetZip(target+location)
assert res.code == "200"

showReturnedZip(res)


