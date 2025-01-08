/** @type {import('tailwindcss').Config} */
export default {
    content: ["./src/main/resources/**/*.{html,js}"],
    theme: {
        extend: {
            spacing: {
                '84': '21rem', // Adding a custom spacing value for 21rem

            },
        },
    },
    plugins: [],
    darkMode: "selector",
};