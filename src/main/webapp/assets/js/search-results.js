document.addEventListener("DOMContentLoaded", function () {
    function formatNumberWithDots(n) {
        if (!n) return '';
        return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }

    function removeDots(str) {
        return str.replace(/\./g, '');
    }

    const priceInputs = document.querySelectorAll('.price-input');

    priceInputs.forEach(input => {
        input.addEventListener('input', function (e) {
            let value = this.value.replace(/\./g, '');
            if (value === '' || isNaN(value)) {
                this.value = '';
                return;
            }
            this.value = formatNumberWithDots(value);
        });

        input.addEventListener('focus', function () {
            this.select();
        });

        input.addEventListener('blur', function () {
            let value = removeDots(this.value);
            if (value && !isNaN(value)) {
                this.value = formatNumberWithDots(value);
            }
        });
    });

    const priceFilterForm = document.getElementById('priceFilterForm');
    if (priceFilterForm) {
        priceFilterForm.addEventListener('submit', function () {
            const minInput = document.getElementById('minPriceInput');
            const maxInput = document.getElementById('maxPriceInput');
            const minHidden = document.getElementById('minPriceHidden');
            const maxHidden = document.getElementById('maxPriceHidden');

            minHidden.value = removeDots(minInput.value);
            maxHidden.value = removeDots(maxInput.value);
        });
    }
});