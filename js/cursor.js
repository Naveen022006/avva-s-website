const cursor = document.querySelector(".cursor");

let mouseX = window.innerWidth / 2;
let mouseY = window.innerHeight / 2;

let currentX = mouseX;
let currentY = mouseY;

let prevX = mouseX;
let prevY = mouseY;

let angle = 0;

const speed = 0.08; // smoothness (lower = smoother)

// TIP OFFSET (adjust if needed)
const offsetX = 6;
const offsetY = 6;

if (cursor) {

    document.addEventListener("mousemove", (e) => {
        mouseX = e.clientX;
        mouseY = e.clientY;
    });

    function animate() {

        // Smooth follow (lerp)
        currentX += (mouseX - currentX) * speed;
        currentY += (mouseY - currentY) * speed;

        // Calculate direction based on smooth movement
        const dx = currentX - prevX;
        const dy = currentY - prevY;

        if (Math.abs(dx) > 0.1 || Math.abs(dy) > 0.1) {
            angle = Math.atan2(dy, dx) * (180 / Math.PI);
        }

        // Apply position + rotation
        cursor.style.transform = `
            translate(${currentX - offsetX}px, ${currentY - offsetY}px)
            rotate(${angle}deg)
        `;

        prevX = currentX;
        prevY = currentY;

        requestAnimationFrame(animate);
    }

    animate();

} else {
    console.warn("Custom cursor element .cursor not found.");
}
