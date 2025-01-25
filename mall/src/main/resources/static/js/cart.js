
class Cart {
    /**
    * 장바구니 모드 가져오기
    * false : 단일 상품 바로 구매
    * true : 장바구니에 담은 상품 구매
    */
    getCartMode() {
        return localStorage.getItem('mode') === 'true';
    }

    setCartMode(mode) {
        localStorage.setItem('mode', mode);
    }

    // 장바구니에 상품 담기
    addProductInCart({id, quantity, noToast}) {
        let products = this.getProductsInCart();
        if (!products) {
            products = [];
        }

        const index = products.findIndex(p => p.id === id);
        if (index !== -1) {
            products[index].quantity = quantity;
        } else {
            products.push({
                id,
                quantity,
            });
        }

        localStorage.setItem('productCart',  JSON.stringify(products));
        if (!noToast) {
            showToast('알림', "장바구니에 해당 상품을 추가하였습니다.");
        }
    }

    // 장바구니에서 상품 빼기
    removeProductInCart(id) {
        const products = this.getProductsInCart();
        const index = products.findIndex(p => p.id === id);
        if (index !== -1) {
            products.splice(index, 1);
        }

        localStorage.setItem('productCart',  JSON.stringify(products));
    }

    // 장바구니 상품 정보 가져오기
    getProductsInCart() {
        return JSON.parse(localStorage.getItem('productCart'));
    }

    // 바로 구매 상품 정보 저장
    saveSingleProduct(product) {
        localStorage.setItem('mode', 'false');
        localStorage.setItem('singleProduct', JSON.stringify(product));
    }

    // 바로 구매 상품 정보 가져오기
    getSingleProduct() {
        return JSON.parse(localStorage.getItem('singleProduct'));
    }
}