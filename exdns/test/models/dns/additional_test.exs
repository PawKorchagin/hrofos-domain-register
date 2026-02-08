defmodule Models.Dns.Net.AdditionalTest do
  use ExUnit.Case, async: true

  alias Models.Dns.Net.Additional
  alias Models.Dns.Net.Additional.{Opt, Record}
  alias Models.Dns.Net.Additional.Opt.{Cookie, Option}

  test "serialize/deserialize additional records" do
    record ${DB_USER:***REMOVED***} %Record{
      name: "extra.example.com",
      type: 1,
      class: 1,
      ttl: 300,
      rdata: <<127, 0, 0, 1>>
    }

    additional ${DB_USER:***REMOVED***} %Additional{records: [record]}

    assert {:ok, bin} ${DB_USER:***REMOVED***} Additional.serialize(additional)
    assert {:ok, ^additional, next} ${DB_USER:***REMOVED***} Additional.deserialize(bin, 0, 1)
    assert next ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end

  test "serialize/deserialize EDNS OPT with DNS cookie" do
    client_cookie ${DB_USER:***REMOVED***} <<1, 2, 3, 4, 5, 6, 7, 8>>
    server_cookie ${DB_USER:***REMOVED***} <<9, 10, 11, 12, 13, 14, 15, 16>>
    cookie_data ${DB_USER:***REMOVED***} client_cookie <> server_cookie

    opt ${DB_USER:***REMOVED***}
      %Opt{
        udp_payload_size: 1232,
        extended_rcode: 0,
        version: 0,
        dnssec_ok: true,
        z: 0,
        options: [
          %Option{
            code: 10,
            data: cookie_data,
            value: %Cookie{client: client_cookie, server: server_cookie}
          }
        ]
      }

    additional ${DB_USER:***REMOVED***} %Additional{opt: opt}

    assert {:ok, bin} ${DB_USER:***REMOVED***} Additional.serialize(additional)
    assert {:ok, parsed, next} ${DB_USER:***REMOVED***} Additional.deserialize(bin, 0, 1)
    assert parsed.opt ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} opt
    assert parsed.records ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} []
    assert next ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} byte_size(bin)
  end
end
