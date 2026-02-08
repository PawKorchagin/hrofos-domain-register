defmodule Models.Dns.Net.RequestTest do
  use ExUnit.Case, async: true

  alias Models.Dns.Net.Additional
  alias Models.Dns.Net.Additional.Opt
  alias Models.Dns.Net.Additional.Opt.{Cookie, Option}
  alias Models.Dns.Net.Additional.Record
  alias Models.Dns.Net.{Header, Question, Request}

  test "serialize/deserialize DNS message" do
    header ${DB_USER:***REMOVED***}
      %Header{
        id: 0x1357,
        qr: 0,
        opcode: 0,
        aa: 0,
        tc: 0,
        rd: 1,
        ra: 0,
        z: 0,
        rcode: 0,
        qdcount: 0,
        ancount: 0,
        nscount: 0,
        arcount: 0
      }

    question ${DB_USER:***REMOVED***} %Question{qname: "www.example.com", qtype: 1, qclass: 1}

    answer_rr ${DB_USER:***REMOVED***} %Record{
      name: "www.example.com",
      type: 1,
      class: 1,
      ttl: 300,
      rdata: <<127, 0, 0, 1>>
    }

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
            data: <<1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16>>,
            value: %Cookie{
              client: <<1, 2, 3, 4, 5, 6, 7, 8>>,
              server: <<9, 10, 11, 12, 13, 14, 15, 16>>
            }
          }
        ]
      }

    request ${DB_USER:***REMOVED***} %Request{
      header: header,
      question: [question],
      answer: [answer_rr],
      authority: [],
      additional: %Additional{records: [], opt: opt}
    }

    assert {:ok, binary} ${DB_USER:***REMOVED***} Request.serialize(request)
    assert {:ok, parsed} ${DB_USER:***REMOVED***} Request.deserialize(binary)

    assert parsed.header.qdcount ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 1
    assert parsed.header.ancount ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 1
    assert parsed.header.nscount ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0
    assert parsed.header.arcount ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 1
    assert parsed.question ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} [question]
    assert parsed.answer ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} [answer_rr]
    assert parsed.additional.opt ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} opt
  end
end
