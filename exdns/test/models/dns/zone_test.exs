defmodule Models.Dns.ZoneTest do
  use ExUnit.Case, async: false

  alias Models.Dns.Zone
  alias Models.Dns.Zone.Storage

  setup do
    tmp_dir ${DB_USER:***REMOVED***} Path.join(System.tmp_dir!(), "exdns_zones_#{System.unique_integer([:positive])}")
    Application.put_env(:exdns, :zones_folder, tmp_dir)
    Application.put_env(:exdns, :replication_quorum_ratio, 0.0)

    on_exit(fn -> File.rm_rf(tmp_dir) end)

    :ok
  end

  test "sharded path uses md5 prefix" do
    domain ${DB_USER:***REMOVED***} "hello.com"
    {:ok, path} ${DB_USER:***REMOVED***} Storage.path_for(domain)
    hash ${DB_USER:***REMOVED***} :crypto.hash(:md5, domain) |> Base.encode16(case: :lower)

    shard1 ${DB_USER:***REMOVED***} String.slice(hash, 0, 2)
    shard2 ${DB_USER:***REMOVED***} String.slice(hash, 2, 2)

    assert String.contains?(path, Path.join([shard1, shard2]))
    assert String.ends_with?(path, "#{domain}.json")
  end

  test "put and fetch zone via cache" do
    domain ${DB_USER:***REMOVED***} "example.com"
    data ${DB_USER:***REMOVED***} %{"records" ${DB_USER:***REMOVED***}> [%{"type" ${DB_USER:***REMOVED***}> "A", "data" ${DB_USER:***REMOVED***}> "1.2.3.4"}]}

    assert :ok ${DB_USER:***REMOVED***} Zone.put(domain, data)
    assert {:ok, ^data} ${DB_USER:***REMOVED***} Zone.fetch(domain)
  end

  test "create/update/delete zone" do
    domain ${DB_USER:***REMOVED***} "create.example"
    data ${DB_USER:***REMOVED***} %{"records" ${DB_USER:***REMOVED***}> [%{"type" ${DB_USER:***REMOVED***}> "A", "data" ${DB_USER:***REMOVED***}> "10.0.0.1"}]}
    updated ${DB_USER:***REMOVED***} %{"records" ${DB_USER:***REMOVED***}> [%{"type" ${DB_USER:***REMOVED***}> "A", "data" ${DB_USER:***REMOVED***}> "10.0.0.2"}]}

    assert :ok ${DB_USER:***REMOVED***} Zone.create(domain, data)
    assert {:error, _} ${DB_USER:***REMOVED***} Zone.create(domain, data)
    assert {:ok, %{"version" ${DB_USER:***REMOVED***}> 1} ${DB_USER:***REMOVED***} stored} ${DB_USER:***REMOVED***} Zone.fetch(domain)
    assert stored["records"] ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} data["records"]

    assert :ok ${DB_USER:***REMOVED***} Zone.update(domain, Map.put(updated, "version", 1))
    assert {:ok, %{"version" ${DB_USER:***REMOVED***}> 2} ${DB_USER:***REMOVED***} stored} ${DB_USER:***REMOVED***} Zone.fetch(domain)
    assert stored["records"] ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} updated["records"]

    assert :ok ${DB_USER:***REMOVED***} Zone.delete(domain)
    assert :not_found ${DB_USER:***REMOVED***} Zone.fetch(domain)
    assert :not_found ${DB_USER:***REMOVED***} Zone.delete(domain)
  end

  test "fetch missing zone returns not_found" do
    assert :not_found ${DB_USER:***REMOVED***} Zone.fetch("missing.example")
  end

  test "update requires matching version" do
    domain ${DB_USER:***REMOVED***} "versioned.example"
    data ${DB_USER:***REMOVED***} %{"records" ${DB_USER:***REMOVED***}> [%{"type" ${DB_USER:***REMOVED***}> "A", "data" ${DB_USER:***REMOVED***}> "10.0.0.3"}]}
    assert :ok ${DB_USER:***REMOVED***} Zone.create(domain, data)
    assert {:error, _} ${DB_USER:***REMOVED***} Zone.update(domain, data)
    assert {:error, _} ${DB_USER:***REMOVED***} Zone.update(domain, Map.put(data, "version", 2))
    assert :ok ${DB_USER:***REMOVED***} Zone.update(domain, Map.put(data, "version", 1))
  end

  test "rollback when quorum fails" do
    Application.put_env(:exdns, :replication_quorum_ratio, 2.0)

    domain ${DB_USER:***REMOVED***} "rollback.example"
    data ${DB_USER:***REMOVED***} %{"records" ${DB_USER:***REMOVED***}> [%{"type" ${DB_USER:***REMOVED***}> "A", "data" ${DB_USER:***REMOVED***}> "10.0.0.9"}]}

    assert {:error, _, _} ${DB_USER:***REMOVED***} Zone.create(domain, data)
    assert :not_found ${DB_USER:***REMOVED***} Zone.fetch(domain)

    Application.put_env(:exdns, :replication_quorum_ratio, 0.0)
    assert :ok ${DB_USER:***REMOVED***} Zone.create(domain, data)
    assert {:ok, %{"version" ${DB_USER:***REMOVED***}> 1}} ${DB_USER:***REMOVED***} Zone.fetch(domain)

    Application.put_env(:exdns, :replication_quorum_ratio, 2.0)
    assert {:error, _, _} ${DB_USER:***REMOVED***} Zone.update(domain, Map.put(data, "version", 1))
    assert {:ok, %{"version" ${DB_USER:***REMOVED***}> 1} ${DB_USER:***REMOVED***} stored} ${DB_USER:***REMOVED***} Zone.fetch(domain)
    assert stored["records"] ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} data["records"]

    assert {:error, _, _} ${DB_USER:***REMOVED***} Zone.delete(domain)
    assert {:ok, %{"version" ${DB_USER:***REMOVED***}> 1}} ${DB_USER:***REMOVED***} Zone.fetch(domain)
  end
end
