import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.system.lsp.databinding.ItemPaymentDetailBinding
import com.system.lsp.ui.viewmodels.PaymentDetail

class PaymentDetailsAdapter(
    private val paymentDetails: MutableList<PaymentDetail> = mutableListOf()
) : RecyclerView.Adapter<PaymentDetailsAdapter.PaymentDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentDetailViewHolder {
        val binding = ItemPaymentDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentDetailViewHolder, position: Int) {
        holder.bind(paymentDetails[position])
    }

    override fun getItemCount(): Int = paymentDetails.size

    fun updateList(newDetails: List<PaymentDetail>) {
        paymentDetails.clear()
        paymentDetails.addAll(newDetails)
        notifyDataSetChanged()
    }

    inner class PaymentDetailViewHolder(private val binding: ItemPaymentDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(detail: PaymentDetail) {
            with(binding) {
                val totalDelayInterestPending = detail.quotaDelayInterestAmount - detail.quotaDelayInterestPaid
                quota.text = "#${detail.quota}"
                quotaDate.text = detail.quotaDate
                quotaTotal.text = detail.quotaTotalAmount.toString()
                quotaDaysDelay.text = "${detail.delayDays} dia(s)"
                quotaTotalDue.text = detail.quotaDelayInterestAmount.toString()
                quotaTotalPaid.text = detail.quotaTotalPendingAmount.toString()
                delayInterest.text = totalDelayInterestPending.toString()

                // Optional: Set click listeners if needed
                root.setOnClickListener {
                    // Handle row click
                }
            }
        }
    }
}
