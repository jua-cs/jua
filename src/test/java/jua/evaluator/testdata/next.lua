t = {"a", "b", "c"}

index, el = next(t)
print(index)
print(el)

index, el = next(t, index)
print(index)
print(el)

index, el = next(t, index)
print(index)
print(el)

index, el = next(t, index)
print(index)
print(el)
