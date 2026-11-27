package com.imd.tripsplit.model

enum class OrigemDespesa {
    MANUAL,
    ROTEIRO
}

data class PagamentoDespesa(
    val id: String,
    val participanteId: String,
    val valor: Double,
)

data class Despesa(
    val id: String,
    val tripId: String,
    val activityId: String? = null,
    val descricao: String,
    val valorTotal: Double,
    val pagadorId: String? = null,
    val participantesIds: List<String> = emptyList(),
    val pagamentos: List<PagamentoDespesa> = emptyList(),
    val origem: OrigemDespesa = OrigemDespesa.MANUAL,
    val data: String,
)

// activityId identifica a atividade do roteiro que originou a despesa.
// pagadorId identifica quem adiantou o dinheiro para pagar a conta.
// pagamentos registra quanto cada participante já devolveu ao responsável pelo pagamento.
