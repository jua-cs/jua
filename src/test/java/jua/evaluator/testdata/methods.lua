local outer = {}
outer.name = 'Mr outer'

local inner = {}
inner.name = 'Mr inner'

outer.inner = inner

function outer:add_outer(v)
    print("called " .. self.name)
    print(self.name)
    self.value = v
end

function outer.inner:add_inner(v)
    print("called " .. self.name)
    self.value = v
end

print("---")
outer:add_outer(5)
print(outer.value)
print(outer.inner.value)
print("---")
outer.inner:add_inner(3)
print(outer.value)
print(outer.inner.value)
print("---")
outer.add_outer(outer.inner, 100)
print(outer.value)
print(outer.inner.value)
print("---")
outer.inner.add_inner(outer, 200)
print(outer.value)
print(outer.inner.value)
print("---")
