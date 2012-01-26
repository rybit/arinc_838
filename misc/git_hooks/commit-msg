#!/usr/bin/env ruby
# vim: set syntax=ruby

denied = <<EOM

Oh snap...

 ______   _______  __    _  ___   _______  ______   __  
|      | |       ||  |  | ||   | |       ||      | |  | 
|  _    ||    ___||   |_| ||   | |    ___||  _    ||  | 
| | |   ||   |___ |       ||   | |   |___ | | |   ||  | 
| |_|   ||    ___||  _    ||   | |    ___|| |_|   ||__| 
|       ||   |___ | | |   ||   | |   |___ |       | __  
|______| |_______||_|  |__||___| |_______||______| |__| 


Did you forget a Tracker ID?
Example: git commit -m "I just fixed a bug. [123456]"

EOM

full_text = File.read(ARGV[0])
if full_text =~ /\[#?\d{6,}\]$/
  exit 0
else
  puts denied
  exit 1
end
