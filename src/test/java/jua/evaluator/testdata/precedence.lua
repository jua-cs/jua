x = {{1,2}}
print(#x)
print(#x[1])

x = {f = function() return 1 end}
print(x.f())

x = {function() return 1 end}
print(x[1]())

f = function() return {1} end

print(f()[1])