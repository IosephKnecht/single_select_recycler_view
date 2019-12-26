package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.annotation.LayoutRes
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

/**
 * Implementation of [AbstractAdapterDelegate] for binding category value.
 *
 * @author IosephKnecht
 */
internal class SelectableCategoryDelegate(
    @LayoutRes private val adapterRes: Int,
    @LayoutRes private val dropDownViewRes: Int
) :
    AbstractAdapterDelegate<SelectableCategoryDelegate.ViewProvider, SelectableViewState> {

    private val factory = SpinnerAdapterFactory(
        adapterRes = adapterRes,
        dropDownViewRes = dropDownViewRes
    )

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val spinner: Spinner

        fun bindSpinnerClickListener(viewState: SelectableViewState) {}
        fun unbindSpinnerClickListener() {}
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            unbindSpinnerClickListener()

            if (spinner.adapter == null) {
                spinner.adapter = factory.getOrCreate(viewProvider.rootView.context)
            }

            spinner.apply {
                setSelection(
                    element.changedLabel.ordinal
                )

                isEnabled = element.isEditableLabel && element.isSelected
            }

            bindSpinnerClickListener(element)
        }
    }

    private class SpinnerAdapterFactory(
        private val adapterRes: Int,
        private val dropDownViewRes: Int
    ) {

        private var adapter: SpinnerAdapter? = null

        fun getOrCreate(context: Context): SpinnerAdapter {
            if (adapter == null) {
                adapter = ArrayAdapter(
                    context,
                    adapterRes,
                    SomeCategory.values().map { it.getTitle(context.resources)!! }
                ).apply {
                    setDropDownViewResource(dropDownViewRes)
                }
            }

            return adapter!!
        }
    }
}