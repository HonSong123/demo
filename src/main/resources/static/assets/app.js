var app = angular.module("productApp", ["ngRoute"]);

app.config(function($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '/layouts/index.html',
            controller: 'ProductController',
        })
        .when('/category', {
            templateUrl: '/layouts/product.html',
            controller: 'ProductController',
        })
        .when('/cart', {
            templateUrl: '/layouts/product-cart.html',
            controller: 'ProductController',
        })
        .when('/checkout', {
            templateUrl: '/layouts/product-checkout.html',
            controller: 'ProductController',
        })
        .when('/product/:id', {
            templateUrl: '/layouts/product-details.html',
            controller: 'ProductDetailController',
        })
        .when('/changepassword', {
            templateUrl: '/layouts/account-login.html',
            controller: 'ProductController',
        })
        .when('/order', {
            templateUrl: '/layouts/order.html',
            controller: 'ProductController',
        })
        // .when('/about', {
        //     templateUrl: 'about.html',
        // })
        .otherwise({
            redirectTo: '/home'
        });
});



app.controller("ProductDetailController", function ($scope, $http, $routeParams) {
    const productId = $routeParams.id;

    $http.get(`http://localhost:8080/api/sanpham/${productId}`)
        .then(function (response) {
            $scope.product = response.data;
        });
    $http.get(`http://localhost:8080/api/sanpham/${productId}/reviews`)
        .then(function (response) {
            $scope.reviews = response.data;
        });
});





// ProductController để lấy danh sách sản phẩm và thêm vào giỏ hàng
app.controller('ProductController', function ($scope,$location, $http, $timeout, $rootScope) {
    let customerId = sessionStorage.getItem('customerId');

    // Khởi tạo mảng sản phẩm
    $scope.products = [];

    // Lấy danh sách sản phẩm từ API
    $scope.initialize = function () {
        $http.get('/api/sanpham')
            .then(function (response) {
                $scope.products = response.data;
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy danh sách sản phẩm:", error);
            });
    };

    $scope.orders=[];
    $scope.order = function () {
        const customerId = sessionStorage.getItem('customerId'); // Lấy customerId từ sessionStorage

        $http.get('/api/checkout/customer/' + customerId)
        .then(function (response) {
            $scope.orders = response.data;
        })
        .catch(function (error) {
            console.log(error);
        });
    };
    $scope.confirmCancel = function (orderId) {
        if (confirm("Are you sure you want to cancel this order?")) {
            $http.delete(`/api/checkout/cancel/${orderId}`)
                .then(function (response) {
                    alert(response.data.message);
                    $scope.order(); // Tải lại danh sách đơn hàng sau khi hủy
                })
                .catch(function (error) {
                    console.log("Error canceling order:", error);
                    alert("Failed to cancel the order. Please try again.");
                });
        }
    };

    $scope.order();


    $scope.minPrice = 0;
    $scope.maxPrice = 1000;

    // Function to filter products by price
    $scope.filterByPrice = function () {
        console.log("Filtering products by price:", $scope.minPrice, $scope.maxPrice);

        // Replace with your API URL
        $http.get('/api/sanpham/filterByPrice?minPrice=' + $scope.minPrice + '&maxPrice=' + $scope.maxPrice)
            .then(function (response) {
                $scope.products = response.data;  // Set the filtered products
            }, function (error) {
                console.error("Error filtering products:", error);
            });
    };

    $scope.addProductToCart = function (product) {
        const customerId = sessionStorage.getItem('customerId'); // Lấy customerId từ sessionStorage

        if (!customerId) {
            // Nếu không có customerId, hiển thị thông báo yêu cầu đăng nhập
            displayAlert("Please log in to add items to the cart.", "danger");
            return; // Dừng lại nếu người dùng chưa đăng nhập
        }

        // Gửi yêu cầu kiểm tra tồn kho
        $http.get(`http://localhost:8080/api/sanpham/stock/${product.productID}`)
            .then(function (response) {
                const stockQuantity = response.data.stockQuantity || 0; // Lấy số lượng tồn kho, mặc định là 0
                if (stockQuantity > 0) {
                    // Nếu còn hàng, thêm sản phẩm vào giỏ với số lượng 1
                    const cartDetail = {
                        productID: product.productID,
                        quantity: 1
                    };

                    $http({
                        method: 'POST',
                        url: `/api/cart/add/${customerId}`,
                        data: cartDetail,
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        }
                    })
                        .then(function (response) {
                            // Nếu thành công, hiển thị thông báo thành công
                            displayAlert("Product added to cart successfully.", "success");

                        })
                        .catch(function (error) {
                            // Nếu có lỗi, hiển thị thông báo lỗi
                            displayAlert("Error adding product to cart.", "danger");
                            console.error("Error adding product to cart:", error);
                        });
                } else {
                    // Nếu hết hàng, hiển thị thông báo
                    displayAlert("Sorry, this product is out of stock.", "warning");
                }
            })
            .catch(function (error) {
                console.error("Error fetching stock information:", error);
                displayAlert("Could not check product stock. Please try again later.", "danger");
            });
    };

// Hàm hiển thị thông báo
    function displayAlert(message, type) {
        // Tạo HTML của thông báo
        var alertHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`;
        // Gắn thông báo vào phần tử alertContainer
        document.getElementById("alertContainer").innerHTML = alertHTML;

        // Tự động ẩn thông báo sau 5 giây
        setTimeout(function () {
            const alertElement = document.querySelector("#alertContainer .alert");
            if (alertElement) {
                alertElement.classList.remove("show");
            }
        }, 2000);
    }


    $scope.cartDetails = [];
    $scope.totalPrice = 0;
    $scope.selectedProducts = [];

    // Hàm để lấy chi tiết giỏ hàng và cập nhật tổng giá
    function loadCartDetails() {
        $http.get("http://localhost:8080/api/cart/details/" + customerId)
            .then(function (response) {
                $scope.cartDetails = response.data;

                // Lấy thêm thông tin tồn kho
                $scope.cartDetails.forEach(item => {
                    $http.get(`http://localhost:8080/api/sanpham/stock/${item.productID}`)
                        .then(function (res) {
                            item.stock = res.data.stockQuantity || 0; // Lưu số lượng tồn kho
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi lấy tồn kho:", error);
                        });
                });

                calculateTotal();
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy chi tiết giỏ hàng:", error);
            });
    }

    $scope.toggleSelection = function (item) {
        // Đảo ngược trạng thái 'selected' của sản phẩm
        item.selected = !item.selected;
        console.log("Item selected:", item.selected);  // Kiểm tra trạng thái sau khi thay đổi

        // Lọc lại danh sách sản phẩm đã chọn
        var selectedProducts = $scope.cartDetails.filter(i => i.selected);
        console.log("Selected Products:", selectedProducts); // Kiểm tra danh sách sản phẩm đã chọn

        // Cập nhật sessionStorage
        sessionStorage.setItem('selectedProducts', JSON.stringify(selectedProducts));
    };

    $scope.proceedToCheckout = function () {
        var selectedProducts = $scope.cartDetails.filter(item => item.selected);
        console.log("Proceed to Checkout - Selected Products:", selectedProducts); // Kiểm tra danh sách sản phẩm đã chọn

        if (selectedProducts.length > 0) { // Kiểm tra có sản phẩm đã chọn
            sessionStorage.setItem('selectedProducts', JSON.stringify(selectedProducts));
            // Chuyển hướng đến trang thanh toán
            window.location.href = '#!/checkout';  // Đảm bảo đường dẫn đúng
        } else {
            alert("Vui lòng chọn sản phẩm để thanh toán.");
        }
    };
    $scope.loadCheckoutPage = function() {
        $scope.selectedProducts = JSON.parse(sessionStorage.getItem('selectedProducts')) || [];
        console.log($scope.selectedProducts); // Kiểm tra dữ liệu đã lấy từ sessionStorage
    };
    
    // Gọi hàm này khi trang thanh toán được tải
    $scope.loadCheckoutPage();


    // $scope.checkoutData = {
    //     customerId: null,
    //     paymentMethod: '',
    //     totalAmount: 0
    // };

    $scope.selectedProducts = JSON.parse(sessionStorage.getItem('selectedProducts')) || [];

    // Calculate the total amount for the checkout
    $scope.calculateTotalAmount = function() {
        $scope.checkoutData.totalAmount = $scope.selectedProducts.reduce((total, product) => total + (product.price * product.quantity), 0);
    };

    $scope.checkout = function () {
        let selectedProducts = $scope.selectedProducts.filter(item => item.selected);  // Lấy từ selectedProducts

        console.log("Selected products:", selectedProducts); // Kiểm tra danh sách sản phẩm được chọn

        const checkoutData = {
            customerId: customerId,
            paymentMethod: $scope.paymentMethod,
            totalAmount: $scope.totalPrice,
            orderDetails: selectedProducts.map(product => ({
                product: { productID: product.productID }, // Chỉ gửi ID của sản phẩm
                quantity: product.quantity
            }))
        };

        console.log("Checkout data sent to API:", checkoutData); // Kiểm tra dữ liệu gửi đi

        $http.post('/api/checkout/process', checkoutData)
            .then(response => {
                alert("Đặt hàng thành công");
                loadCartDetails(); // Làm mới giỏ hàng sau khi thanh toán
            })
            .catch(error => {
                console.error("Checkout error:", error);
                alert("Checkout failed.");
            });
    };






    $scope.customer = {};
    $scope.successMessage = "";
    $scope.errorMessage = "";
    // Load Customer Information
    $scope.loadCustomerInfo = function() {
        const customerId = sessionStorage.getItem('customerId');
        $http.get(`/api/customer/${customerId}`)
            .then(function(response) {
                $scope.customer = response.data;
            })
            .catch(function(error) {
                console.error("Error loading customer information:", error);
            });
    };


    function calculateTotal() {
        $scope.totalPrice = $scope.cartDetails.reduce((total, item) => total + item.price * item.quantity, 0);
    }
    $scope.reloadPage = function () {
        window.location.reload();
    };

    // Update Customer Information
    $scope.updateCustomerInfo = function() {
        $http.put(`/api/customer/update`, $scope.customer)
            .then(function(response) {
                $scope.successMessage = "Customer information updated successfully!";
                $scope.errorMessage = "";
                $scope.customer = response.data; // Refresh customer data after save
            })
            .catch(function(error) {
                $scope.errorMessage = "Failed to update customer information.";
                $scope.successMessage = "";
                console.error("Error updating customer information:", error);
            });
    };

    // Load customer info on page load
    $scope.loadCustomerInfo();
    

    // Cập nhật số lượng sản phẩm
    $scope.updateQuantity = function (cartItem) {
        if (cartItem.quantity <= 0) {
            console.warn("Số lượng không hợp lệ.");
            return;
        }

        cartItem.totalPrice = cartItem.price * cartItem.quantity;
        calculateTotal();

        $http.post("http://localhost:8080/api/cart/update", {
            productId: cartItem.productID,
            quantity: cartItem.quantity,
            customerId: customerId
        })
            .then(function (response) {
                loadCartDetails();
                console.log("Cập nhật số lượng thành công:", response);
                calculateTotal();
            })
            .catch(function (error) {
                console.error("Lỗi khi cập nhật số lượng:", error);
            });
    };

    // Xóa sản phẩm khỏi giỏ hàng
    $scope.removeProduct = function (productId) {
        $http.delete(`http://localhost:8080/api/cart/remove/${productId}?customerId=${customerId}`)
            .then(function (response) {
                $scope.cartDetails = $scope.cartDetails.filter(item => item.productID !== productId);
                calculateTotal();
                loadCartDetails();
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa sản phẩm:", error);
            });
    };

    $scope.increaseQuantity = function (item) {
        let newQuantity = item.quantity + 1;

        $http.post("http://localhost:8080/api/cart/update", {
            productId: item.productID,
            quantity: newQuantity,
            customerId: customerId
        })
            .then(function (response) {
                if (response.data && response.data.message) {
                    // Hiển thị thông báo thành công
                    item.quantity = newQuantity; // Cập nhật số lượng
                    loadCartDetails(); // Cập nhật giỏ hàng
                }
            })
            .catch(function (error) {
                if (error.status === 400) {
                    // Hiển thị thông báo hết hàng
                } else {
                    console.error("Lỗi khi cập nhật số lượng:", error);

                }
            });
    };




    $scope.decreaseQuantity = function (item) {
        if (item.quantity > 1) {
            item.quantity--;
            $scope.updateQuantity(item);
        } else {
            console.warn("Số lượng tối thiểu là 1.");
        }
    };

    // Tính tổng giá của giỏ hàng
   

    // Nghe sự kiện khi giỏ hàng được cập nhật

    $scope.initialize();
     // Khởi động khi tải trang


    $scope.categories = [];

    $scope.searchKeyword = '';
    $scope.selectedCategory = null;

    // Lấy danh mục
    $http.get('http://localhost:8080/api/category/all')
        .then(function(response) {
            $scope.categories = response.data;
        })
        .catch(function(error) {
            console.error("Lỗi khi tải danh mục:", error);
        });

    // Tìm kiếm sản phẩm
    $scope.searchProducts = function(keyword, categoryID) {
        const params = {
            keyword: keyword || '',
            categoryID: categoryID || null
        };

        $http.get('http://localhost:8080/api/sanpham/search', { params })
            .then(function(response) {
                $scope.products = response.data;
                console.log("Kết quả tìm kiếm:", response.data);
            })
            .catch(function(error) {
                console.error("Lỗi khi tìm kiếm sản phẩm:", error);
            });
    };

    // Lọc sản phẩm theo danh mục
    $scope.fetchProductsByCategory = function(categoryID) {
        $scope.searchProducts($scope.searchKeyword, categoryID);
    };

    // Function to load all categories
    $scope.loadCategories = function () {
        $http.get('/api/category')
            .then(function (response) {
                $scope.categories = response.data;
            })
            .catch(function (error) {
                console.error("Error loading categories:", error);
            });
    };

    // Function to fetch products by selected category
    $scope.fetchProductsByCategory = function (categoryID) {
        if (!categoryID) return;

        $http.get(`/api/category/${categoryID}/products`)
            .then(function (response) {
                $scope.products = response.data;
            })
            .catch(function (error) {
                console.error("Error loading products by category:", error);
            });
    };

    // Load categories on page load
    $scope.loadCategories();




    //Lọc giá
    $scope.filterByPrice = function () {
        const minPrice = $scope.minPrice || 0; // Giá tối thiểu mặc định là 0
        const maxPrice = $scope.maxPrice || Number.MAX_SAFE_INTEGER; // Giá tối đa mặc định là cực lớn

        $http.get(`http://localhost:8080/api/sanpham/filter?minPrice=${minPrice}&maxPrice=${maxPrice}`)
            .then(function (response) {
                $scope.products = response.data; // Cập nhật danh sách sản phẩm
            })
            .catch(function (error) {
                console.error("Lỗi khi lọc sản phẩm:", error);
            });
    };




    function loadCustomerAndOrderDetails() {
        const customerId = sessionStorage.getItem('customerId');

        // Load customer information
        $http.get(`/api/customer/${customerId}`)
            .then(response => {
                $scope.customer = response.data;
            })
            .catch(error => console.error("Error loading customer info:", error));

        // Load order details (selected products)
        const selectedProducts = JSON.parse(sessionStorage.getItem('selectedProducts')) || [];
        $scope.orderDetails = selectedProducts;

        // Calculate total amount
        $scope.totalAmount = selectedProducts.reduce((sum, product) => sum + product.price * product.quantity, 0);
    }

    // Call the function to load data when the controller is initialized
    loadCustomerAndOrderDetails();

    $scope.viewProductDetail = function (productId) {
        // Redirect to the product details page, passing the product ID
        window.location.href = '/product-details?productID=' + productId;
    };



    $scope.applyDiscount = function () {
        if ($scope.discountCode) {
            // Gửi mã giảm giá lên backend để kiểm tra
            $http.post('http://localhost:8080/api/promotion/apply', {
                discountCode: $scope.discountCode
            })
                .then(function (response) {
                    // Giảm giá hợp lệ, cập nhật tổng số tiền
                    if (response.data.discount) {
                        var discount = response.data.discount;
                        var totalPrice = 0;

                        // Kiểm tra và xử lý giỏ hàng
                        $scope.cartDetails.forEach(function(item) {
                            // Nếu sản phẩm có giá trực tiếp mà không cần thuộc tính 'product'
                            if (item.price) {
                                var productPrice = item.price;
                                // Giảm giá theo tỷ lệ
                                var discountedPrice = productPrice - (productPrice * discount / 100);
                                item.discountedPrice = discountedPrice;  // Cập nhật giá sản phẩm sau giảm
                                totalPrice += discountedPrice * item.quantity;
                            } else {
                                console.warn("Sản phẩm không có giá hoặc thông tin không đầy đủ", item);
                            }
                        });

                        // Cập nhật lại tổng số tiền
                        $scope.totalAmount = totalPrice;
                        alert("Mã giảm giá áp dụng thành công!");
                    } else {
                        alert("Mã giảm giá không hợp lệ.");
                    }
                })
                .catch(function (error) {
                    console.error("Lỗi khi áp dụng mã giảm giá:", error);
                    alert("Có lỗi xảy ra khi áp dụng mã giảm giá.");
                });
        } else {
            alert("Vui lòng nhập mã giảm giá.");
        }
    };


    //Đổi mật khẩu
    $scope.changePassword = function() {
        const customerId = parseInt(sessionStorage.getItem('customerId'), 10);
        if (!customerId) {
            $scope.displayalert("Please log in", "danger");
            return;
        }

        if ($scope.newPassword !== $scope.confirmPassword) {
            $scope.displayalert("Mật khẩu mới và xác nhận mật khẩu không khớp.", "danger");
            return;
        }

        const payload = {
            customerId: customerId,
            oldPassword: $scope.oldPassword,
            newPassword: $scope.newPassword,
            confirmPassword: $scope.confirmPassword
        };

        $http.post("/api/auth/change-password", payload)
            .then(function(response) {
                $scope.displayalert("Đổi mật khẩu thành công.", "success");
            })
            .catch(function(error) {
                $scope.displayalert("Có lỗi xảy ra khi đổi mật khẩu.", "danger");
                console.error(error);
            });
    };



    $scope.displayalert = function(message, type) {
        // Đặt thông báo và kiểu
        $scope.alert = {
            message: message,
            type: type
        };

        // Đảm bảo AngularJS cập nhật giao diện
        if (!$scope.$$phase) {
            $scope.$apply();
        }

        // Tự động ẩn thông báo sau 2 giây
        setTimeout(function() {
            $scope.alert = {}; // Xóa thông báo sau 2 giây
            $scope.$apply();  // Cập nhật giao diện
        }, 2000);
    };




    // Function to navigate to the product detail page
  
    $scope.initialize();
    loadCartDetails()




    $scope.isLoggedIn = !!sessionStorage.getItem('customerId');
    $scope.userName = sessionStorage.getItem('customerName') || "Người dùng";
    $scope.isLoggedIn = false;

    $http.get('/api/auth/current-user').then(function (response) {
        if (response.data) {
            $scope.isLoggedIn = true;
            $scope.user = response.data;
        }
    });

    const productId = 1; // ID sản phẩm (ví dụ)
    $scope.reviews = [];
    $scope.newReview = {
        rating: 5, // Mặc định là 5 sao
        reviewtext: '',
        customer: {
            name: '',
            email: ''
        },
        product: { productid: productId }
    };

    // Kết nối WebSocket
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function() {
        stompClient.subscribe('/topic/reviews/' + productId, function(message) {
            const review = JSON.parse(message.body);
            $scope.$apply(function() {
                $scope.reviews.push(review); // Thêm bình luận mới
            });
        });
    });

    // Tải danh sách bình luận
    $http.get('/api/sanpham/' + productId).then(function(response) {
        $scope.reviews = response.data;
    });

    // Gửi bình luận
    $scope.submitReview = function() {
        $http.post('/api/sanpham/add', $scope.newReview).then(function(response) {
            $scope.newReview.reviewtext = ''; // Reset text
        });
    };
   
    // Hàm đăng xuất
    $scope.logout = function() {
        sessionStorage.removeItem('customerId');
        sessionStorage.removeItem('customerName');
        $scope.isLoggedIn = false;
        alert("Bạn đã đăng xuất thành công.");
        window.location.href = "/"; // Quay lại trang đăng nhập
    };
    }); 
  




