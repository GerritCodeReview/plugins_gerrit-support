require 'net/http'
require 'json'


def doQuery(target, query)
  url = URI.parse(target)
  req = Net::HTTP::Post.new(url.to_s, 'Content-Type' => 'application/json')
  req.body = query.to_json
  req.basic_auth 'admin', 'secret'
  res = Net::HTTP.start(url.host, url.port) {|http|
    http.request(req)
  }
  return res
end

def doGetZip(target)
  url = URI.parse(target)
  req = Net::HTTP::Get.new(url.to_s)
  req.basic_auth 'admin', 'secret'
  res = Net::HTTP.start(url.host, url.port) {|http|
    http.request(req)
  }
  return res
end
