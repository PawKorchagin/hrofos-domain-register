defmodule Models.Dns.RrTest do
  use ExUnit.Case, async: true

  alias Models.Dns.Rr.{A, Aaaa, Mx, Ns, Ptr, Soa, Txt}

  test "A record serialize/deserialize roundtrip" do
    rr ${DB_USER:***REMOVED***} %A{name: "example.com", class: 1, ttl: 3600, address: {1, 2, 3, 4}}
    assert {:ok, bin} ${DB_USER:***REMOVED***} A.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} A.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "AAAA record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Aaaa{
      name: "ipv6.example.com",
      class: 1,
      ttl: 7200,
      address: {0x2001, 0x0db8, 0, 0, 0, 0, 0, 0x1}
    }

    assert {:ok, bin} ${DB_USER:***REMOVED***} Aaaa.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Aaaa.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "NS record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Ns{name: "example.com", class: 1, ttl: 600, target: "ns1.example.com"}
    assert {:ok, bin} ${DB_USER:***REMOVED***} Ns.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Ns.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "PTR record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Ptr{name: "4.3.2.1.in-addr.arpa", class: 1, ttl: 86_400, ptrdname: "example.com"}
    assert {:ok, bin} ${DB_USER:***REMOVED***} Ptr.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Ptr.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "MX record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Mx{
      name: "example.com",
      class: 1,
      ttl: 300,
      preference: 10,
      exchange: "mail.example.com"
    }

    assert {:ok, bin} ${DB_USER:***REMOVED***} Mx.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Mx.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "TXT record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Txt{
      name: "example.com",
      class: 1,
      ttl: 1_800,
      strings: ["v${DB_USER:***REMOVED***}spf1 include:example.net", "ip4:192.0.2.0/24"]
    }

    assert {:ok, bin} ${DB_USER:***REMOVED***} Txt.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Txt.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "SOA record roundtrip" do
    rr ${DB_USER:***REMOVED***} %Soa{
      name: "example.com",
      class: 1,
      ttl: 86_400,
      mname: "ns1.example.com",
      rname: "hostmaster.example.com",
      serial: 20_260_109,
      refresh: 7200,
      retry: 3600,
      expire: 1_209_600,
      minimum: 3600
    }

    assert {:ok, bin} ${DB_USER:***REMOVED***} Soa.serialize(rr)
    assert {:ok, ^rr, size} ${DB_USER:***REMOVED***} Soa.deserialize(bin, 0)
    assert size ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end
end
