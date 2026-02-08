defmodule Models.Dns.Rr.Types do
  @moduledoc false

  @type_map %{
    "A" ${DB_USER:***REMOVED***}> 1,
    "NS" ${DB_USER:***REMOVED***}> 2,
    "CNAME" ${DB_USER:***REMOVED***}> 5,
    "SOA" ${DB_USER:***REMOVED***}> 6,
    "PTR" ${DB_USER:***REMOVED***}> 12,
    "MX" ${DB_USER:***REMOVED***}> 15,
    "TXT" ${DB_USER:***REMOVED***}> 16,
    "AAAA" ${DB_USER:***REMOVED***}> 28
  }

  @spec code(String.t() | non_neg_integer()) :: {:ok, non_neg_integer()} | {:error, String.t()}
  def code(type) when is_integer(type) and type in 0..0xFFFF, do: {:ok, type}
  def code(type) when is_integer(type), do: {:error, "type must be between 0 and 65535"}

  def code(type) when is_binary(type) do
    case Map.fetch(@type_map, String.upcase(type)) do
      {:ok, value} -> {:ok, value}
      :error -> {:error, "unsupported record type #{inspect(type)}"}
    end
  end

  def code(nil), do: {:error, "type is required"}
  def code(_), do: {:error, "type is required"}
end
