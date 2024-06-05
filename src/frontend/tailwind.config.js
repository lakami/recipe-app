/** @type {import('tailwindcss').Config} */
module.exports = {
  presets: [require('@spartan-ng/ui-core/hlm-tailwind-preset')],
  content: [
    './src/**/*.{html,ts}',
    './spartans/**/*.{html,ts}',
  ],
  theme: {
    extend: {
      boxShadow: {
        'bottom': '0 .125rem .375rem rgba(var(--header-shadow-color-rgb), .15)',
      },
      fontFamily: {
        'sans': ['Dm Sans', 'Trebuchet MS', 'sans-serif'],
        'serif': ['Argos', 'Palatino', 'serif']
      }
    },
  },
  plugins: [],
};
