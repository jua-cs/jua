x = {1, 2, 3}
print(x == {1, 2, 3})

function inspect(count)
    print("inspect number: " .. count)
    print(x[0])
    print(x[1])
    print(x[2])
    print(x[3])
    print(x[4])
    print(x[5])
    print(x[100])
    print(x['test'])
    print(x['test2'])
    print(#x)
    print("---")
end
inspect(1)

x[1] = 'hello'
inspect(2)

x['test'] = 'test-string'
inspect(3)

x.test = 'cc'
inspect(4)

x.test2 = 'new'
inspect(5)

table.remove(x, 1)
inspect(6)

table.insert(x, "last")
inspect(7)

x[#x + 1] = "new last"
inspect(8)

print(table.concat(x, "|"))

x.func = function() print("hi") end
x.func()

x = {1, 2, 3}
inspect(9)

x[100] = 100
inspect(10)

x[5] = 5
inspect(11)

x[4] = 4
inspect(12)

print(#{hello = 1, hi = 2})
print(#{[4.5] = 1, [3.2] = 2})
