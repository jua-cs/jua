-- defines a factorial function
function fact (n)
  if n == 0 then
    return 1
  else
    return n * fact(n-1)
  end
end

print(fact(5))
print(fact(10))
-- Should ignore the print(2) parameter but still execute it
print(fact(10, print(2)))
