require 'zipruby'

def showReturnedZip(response)
  Zip::Archive.open_buffer(response.body) do |archive|
    archive.each do |entry|
      puts ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+entry.name + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

      begin
        read = entry.read
        obj = JSON.parse(read)
        pretty_str = JSON.pretty_unparse(obj)
        puts pretty_str
      rescue Exception => e
        puts read
      end
    end
  end

end