defmodule Models.Dns.Rr.Ns do
  @moduledoc false
  alias Models.Dns.Name
  alias Models.Dns.Rr.Common

  @type t :: %__MODULE__{
          name: String.t(),
          class: non_neg_integer(),
          ttl: non_neg_integer(),
          target: String.t()
        }

  defstruct name: ".", class: 1, ttl: 0, target: "."

  @type_code 2

  @spec serialize(t()) :: {:ok, binary()} | {:error, String.t()}
  def serialize(%__MODULE__{} ${DB_USER:***REMOVED***} rr) do
    with {:ok, target_wire} <- Name.encode(rr.target) do
      Common.serialize_rr(rr, @type_code, target_wire)
    end
  end

  @spec deserialize(binary(), non_neg_integer()) ::
          {:ok, t(), non_neg_integer()} | {:error, String.t()}
  def deserialize(message, offset \\ 0) do
    with {:ok, header} <- Common.deserialize_rr(message, offset, @type_code),
         {:ok, target, cursor} <- Name.decode(message, header.rdata_offset),
         :ok <- ensure_consumed(cursor, header) do
      rr ${DB_USER:***REMOVED***} %__MODULE__{
        name: header.name,
        class: header.class,
        ttl: header.ttl,
        target: target
      }

      {:ok, rr, header.next_offset}
    end
  end

  defp ensure_consumed(cursor, %{rdata_offset: start, rdlength: len}) do
    if cursor - start ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} len do
      :ok
    else
      {:error, "ns rdata length mismatch"}
    end
  end
end
