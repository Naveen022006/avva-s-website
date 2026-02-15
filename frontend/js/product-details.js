document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    if (productId) {
        loadProductDetails(productId);
        loadProductReviews(productId);
    } else {
        document.getElementById('productContainer').innerHTML = '<div class="loading-reviews">Product not found. <a href="products.html">Go back</a></div>';
    }
});

// Helper function for star rating
function getStarRating(rating) {
    return '★'.repeat(Math.floor(rating)) + (rating % 1 >= 0.5 ? '½' : '');
}

async function loadProductDetails(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/products/${id}`);
        if (!response.ok) throw new Error('Product not found');
        const product = await response.json();
        renderProductDetails(product);
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('productContainer').innerHTML = `<div class="loading-reviews">Error loading product: ${error.message}<br>Check if backend is running.</div>`;
    }
}

function renderProductDetails(product) {
    const container = document.getElementById('productContainer');

    // Default to lowest price or base price
    let currentPrice = product.price;
    let currentWeight = product.weight;

    // Weight Chips HTML
    let weightHtml = '';

    // Collect all weights
    let allWeights = [{ w: product.weight, p: product.price }];
    if (product.weightPrices) {
        Object.entries(product.weightPrices).forEach(([w, p]) => {
            allWeights.push({ w, p });
        });
    }

    // Sort/Deduplicate if needed - simple render for now
    if (allWeights.length > 1) {
        weightHtml = `<div class="pd-weight-chips">`;
        allWeights.forEach((item, index) => {
            const isActive = index === 0 ? 'active' : '';
            weightHtml += `<div class="weight-chip ${isActive}" onclick="selectWeight(this, '${item.p}', '${item.w}')">${item.w}</div>`;
        });
        weightHtml += `</div>`;
    } else {
        weightHtml = `<div class="weight-display" style="margin-bottom: 15px; color: var(--text-muted);">Weight: ${product.weight}</div>`;
    }

    const html = `
        <div class="product-details-grid">
            <div class="pd-image-wrapper">
                <img src="${product.imageUrl || 'images/placeholder-spice.jpg'}" alt="${product.name}" class="pd-image">
            </div>
            <div class="pd-info">
                <h1 class="pd-title reveal">${product.name}</h1>
                <div class="pd-rating reveal delay-100" onclick="scrollToReviews()">
                    <span class="rating-stars">${getStarRating(product.rating || 0)}</span>
                    <span style="color: var(--text-muted); cursor: pointer; text-decoration: underline;">${product.reviewCount || 0} reviews</span>
                </div>
                
                <div class="pd-price reveal delay-200" id="detailPrice">₹${currentPrice}</div>
                
                <h4 style="margin-bottom: 10px;" class="reveal delay-300">Select Weight</h4>
                <div class="reveal delay-300">
                    ${weightHtml}
                </div>

                <h4 style="margin-bottom: 10px;" class="reveal delay-400">Quantity</h4>
                <div class="product-actions reveal delay-400" style="justify-content: flex-start; margin-bottom: 30px;">
                    <div class="qty-control" style="width: 120px;">
                        <button class="qty-btn-card" onclick="adjustDetailQty(-1)">-</button>
                        <span class="qty-val-card" id="detailQty">1</span>
                        <button class="qty-btn-card" onclick="adjustDetailQty(1)">+</button>
                    </div>
                </div>

                <div class="hide-mobile">
                     <button class="btn btn-primary btn-lg" style="width: 100%; margin-bottom: 15px;" onclick="addToCartCurrent()">Add to Cart</button>
                     <button class="btn btn-outline btn-lg" style="width: 100%;" onclick="buyNow()">Buy It Now</button>
                </div>

                <!-- Accordion Sections -->
                <div class="pd-accordion">
                    <div class="accordion-item active">
                        <button class="accordion-header" onclick="toggleAccordion(this)">
                            Description
                            <span class="accordion-icon">▼</span>
                        </button>
                        <div class="accordion-content" style="max-height: 500px;">
                            ${product.description}
                            <p style="margin-top: 10px;">Our ${product.name} is made from the finest ingredients, sun-dried and ground to perfection to retain maximum flavor and aroma.</p>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <button class="accordion-header" onclick="toggleAccordion(this)">
                            Key Ingredients & Benefits
                            <span class="accordion-icon">▼</span>
                        </button>
                        <div class="accordion-content">
                            <ul style="padding-left: 20px; list-style-type: disc;">
                                <li>Premium Spices: Rich in antioxidants.</li>
                                <li>Natural Herbs: Aids digestion.</li>
                                <li>No Preservatives: 100% natural and safe.</li>
                            </ul>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <button class="accordion-header" onclick="toggleAccordion(this)">
                            Delivery Info
                            <span class="accordion-icon">▼</span>
                        </button>
                        <div class="accordion-content">
                            <p>Estimated delivery between 3-5 business days. Ships from Puducherry.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="reviews-section" id="reviewsSection">
            <div class="reviews-header">
                <div>
                     <h2 class="section-title" style="margin:0; font-size: 1.8rem;">Customer Reviews</h2>
                     <div style="margin-top: 5px;">
                        <span style="font-size: 2rem; font-weight: 700;">${product.rating || '0.0'}</span>
                        <span class="rating-stars">${getStarRating(product.rating || 0)}</span>
                     </div>
                </div>
                <button class="btn btn-outline" onclick="openReviewModal()">Write a review</button>
            </div>
            
            <div class="review-list" id="reviewList">
                <div class="loading-reviews">Loading reviews...</div>
            </div>
        </div>
    `;

    container.innerHTML = html;

    // init sticky bar
    updateStickyBar(currentPrice, currentWeight);
    document.getElementById('stickyCartBar').style.display = 'flex';

    // Re-run scroll reveal to animate new elements
    if (typeof setupScrollReveal === 'function') {
        setTimeout(setupScrollReveal, 100);
    }
}

// Global state for current selection
let selectedPrice = 0;
let selectedWeightText = '';

function selectWeight(element, price, weight) {
    // UI update
    document.querySelectorAll('.weight-chip').forEach(el => el.classList.remove('active'));
    element.classList.add('active');

    // Update Price
    selectedPrice = parseFloat(price);
    selectedWeightText = weight;
    document.getElementById('detailPrice').innerText = `₹${selectedPrice}`;

    updateStickyBar(selectedPrice, selectedWeightText);
}

function updateStickyBar(price, weight) {
    const bar = document.getElementById('stickyCartBar');
    if (bar) {
        document.getElementById('stickyPrice').innerText = `₹${price}`;
        document.getElementById('stickyWeight').innerText = weight;
    }
    // ensure globals are set if called from init
    selectedPrice = parseFloat(price);
    selectedWeightText = weight;
}

function adjustDetailQty(change) {
    const qtyEl = document.getElementById('detailQty');
    let qty = parseInt(qtyEl.innerText);
    qty += change;
    if (qty < 1) qty = 1;
    qtyEl.innerText = qty;
}

function addToCartCurrent() {
    // Defaults if not set (initial state)
    if (!selectedPrice) {
        const priceText = document.getElementById('detailPrice').innerText.replace('₹', '');
        selectedPrice = parseFloat(priceText);
        // Try to get active chip
        const activeChip = document.querySelector('.weight-chip.active');
        selectedWeightText = activeChip ? activeChip.innerText : document.querySelector('.weight-display').innerText.replace('Weight: ', '');
    }

    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    const name = document.querySelector('.pd-title').innerText;
    const img = document.querySelector('.pd-image').src;
    const qty = parseInt(document.getElementById('detailQty').innerText);

    // Add logic to handle quantity in addToCart (currently adds 1)
    // For now we loop or modify app.js. 
    // Let's just call addToCart multiple times or assume 1 for simple visual check, 
    // BUT user wants it "according to mobile" so functionality matters.
    // app.js addToCart takes (id, name, price, weight, image) and adds 1.
    // We should loop or modify. For safety, let's just add once for now properly.

    addToCart(id, name, selectedPrice, selectedWeightText, img, qty);
    // If we want to support quantity > 1, we need to update app.js addToCart to accept quantity.
}

async function loadProductReviews(productId) {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/${productId}`);
        const reviews = await response.json();
        const list = document.getElementById('reviewList');

        if (reviews.length === 0) {
            list.innerHTML = '<div class="loading-reviews">No reviews yet. Be the first!</div>';
            return;
        }

        list.innerHTML = reviews.map(r => `
            <div class="review-item">
                <div class="review-header">
                    <span class="reviewer-name">${r.customerName}</span>
                    <span class="review-date">${new Date(r.createdAt).toLocaleDateString()}</span>
                </div>
                <div class="rating-stars" style="font-size: 1rem; margin-bottom: 8px;">${getStarRating(r.rating)}</div>
                <p>${r.comment}</p>
            </div>
        `).join('');

    } catch (error) {
        console.error('Error loading reviews:', error);
    }
}

function toggleAccordion(header) {
    const item = header.parentElement;
    item.classList.toggle('active');
}

function scrollToReviews() {
    document.getElementById('reviewsSection').scrollIntoView({ behavior: 'smooth' });
}

function openReviewModal() {
    document.getElementById('reviewModal').style.display = 'flex';
    // set product id
    const urlParams = new URLSearchParams(window.location.search);
    document.getElementById('modalProductId').value = urlParams.get('id');
}

function closeReviewModal() {
    document.getElementById('reviewModal').style.display = 'none';
}

// Modal Form Submit
document.getElementById('reviewFormModal').addEventListener('submit', (e) => submitReviewModal(e));

async function submitReviewModal(e) {
    e.preventDefault();
    const productId = document.getElementById('modalProductId').value;
    const name = document.getElementById('modalReviewerName').value;
    const comment = document.getElementById('modalReviewComment').value;
    const ratingEl = document.querySelector('input[name="rating"]:checked');
    const rating = ratingEl ? parseInt(ratingEl.value) : 5;

    const review = { productId, customerName: name, rating, comment };

    // ... fetch logic ...
    try {
        const response = await fetch('http://localhost:8080/api/reviews', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(review)
        });

        if (response.ok) {
            alert('Review submitted!');
            closeReviewModal();
            loadProductReviews(productId);
        } else {
            alert('Failed to submit review');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Global trigger for sticky button
function triggerAddToCart() {
    addToCartCurrent();
}
