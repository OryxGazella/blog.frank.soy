Time::DATE_FORMATS[:pretty] = lambda { |time| time.strftime("%a, %b %e %G at %l:%M") + time.strftime("%p").downcase }
