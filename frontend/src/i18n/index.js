import { reactive, computed } from 'vue'

const translations = {
  uz: {
    nav: { home: 'Bosh oyna', about: 'Haqimizda', menu: 'Menyu', news: 'Yangiliklar', contact: 'Aloqa', cart: 'Savatcha', orders: 'Buyurtmalarim', login: 'Kirish', register: 'Ro\'yxat' },
    hero: {
      badge: '✨ Toshkentning eng yaxshi restorani',
      title: 'Restoranimizga Xush Kelibsiz!',
      sub: 'Har bir taom yuraklardan chiqqan mehr bilan tayyorlanadi. Milliy va xalqaro taomlarning betakror uyg\'unligini bizda his qiling.',
      btn_menu: '🍽 Menyuni ko\'rish',
      btn_about: 'Biz haqimizda',
      stat_dishes: 'Taomlar',
      stat_clients: 'Mijozlar',
      stat_experience: 'Yillik tajriba'
    },
    home: {
      services_badge: 'Bizning xizmatlar',
      services_title: 'Sizni nima kutmoqda?',
      services_sub: 'Eng yaxshi taomlar, qulaylik va unutilmas tajriba',
      about_title: 'Bizning restoran tarixi va an\'analari',
      about_desc: 'Obsidian 2009-yilda tashkil etilgan va o\'n besh yil davomida Toshkentning eng mashhur restoranlaridan biriga aylangan.',
      menu_title: 'Milliy taomlardan tortib xalqaro noz-ne\'matlargacha',
      menu_desc: 'Bizning kitob ko\'rinishidagi menyumiz orqali milliy taomlar, turk oshxonasi va ichimliklarni ko\'ring.',
      news_title: 'Restoranimizdagi so\'nggi yangiliklar',
      news_desc: 'Yangi taomlar va maxsus takliflardan xabardor bo\'lib turing.',
      contact_title: 'Biz Toshkentning markazida joylashganmiz',
      contact_desc: 'Yunusobod tumanida, qulay joylashuvda bizni toping.',
      more: 'Batafsil →'
    },
    about: {
      hero_title: 'Obsidian - Toshkentning durdonasi',
      hero_sub: '2009-yildan buyon sifat, mehribonlik va betakror taomlar bilan sizga xizmat qilib kelmoqdamiz.',
      story_badge: 'Bizning hikoyamiz',
      story_title: '15 yillik an\'ana va muhabbat',
      story_p1: 'Obsidian 2009-yilda kichik bir oilaviy restoran sifatida o\'z faoliyatini boshladi.',
      story_p2: 'Bugun biz 500 dan ortiq mehmon sig\'imiga ega, Toshkentning eng mashhur restoranlaridan biriga aylandik.',
      value_quality: 'Sifat',
      value_quality_desc: 'Faqat eng yangi mahsulotlar',
      value_skill: 'Mahorat',
      value_skill_desc: 'Tajribali oshpazlar',
      value_natural: 'Tabiiylik',
      value_natural_desc: 'Sun\'iy bo\'yoqsiz taomlar',
      value_award: 'Tan olish',
      value_award_desc: 'Ko\'plab mukofotlar sohibi',
      stats_exp: 'Yillik tajriba',
      stats_dishes: 'Taomlar assortimenti',
      stats_clients: 'Mamnun mijozlar',
      stats_chefs: 'Professional oshpaz',
      cta_title: 'Bizni tashrif buyuring!',
      cta_sub: 'Har kun soat 10:00 dan 23:00 gacha ochiq. Bron qilish uchun bog\'laning.'
    },
    menu: {
      title: 'Restoran Menyusi',
      sub: 'Milliy taomlardan tortib xalqaro noz-ne\'matlargacha',
      sections: 'Bo\'limlar',
      open_hint: 'Ochish uchun bosing →',
      close: 'Yopish',
      select_cat: 'Chap tarafdan bo\'lim tanlang',
      empty: 'Bu bo\'limda hozircha taomlar yo\'q',
      page: 'bet',
      add: 'Savat',
      added: 'Qo\'shildi'
    },
    cart: {
      title: 'Savatcha',
      empty: 'Savatcha bo\'sh',
      total: 'Jami',
      checkout: 'Buyurtmani tasdiqlash',
      type: 'Buyurtma turi',
      dining: 'Restaranda',
      delivery: 'Dastavka',
      payment: 'To\'lov turi',
      cash: 'Naqd pul',
      card: 'Karta orqali',
      time: 'Yetkazib berish vaqti',
      asap: 'Tez orada',
      ai_title: 'AI Sizga tavsiya qiladi',
      address_label: 'Yetkazib berish manzili',
      address_select: 'Manzilni xaritada tanlang',
      btn_order: 'Buyurtma berish'
    },
    footer: {
      pages: 'Sahifalar',
      contact: 'Aloqa',
      address: 'Manzil',
      rights: 'Barcha huquqlar himoyalangan.'
    },
    auth: { login: 'Kirish', register: 'Ro\'yxatdan o\'tish', logout: 'Chiqish' }
  },
  ru: {
    nav: { home: 'Главная', about: 'О нас', menu: 'Меню', news: 'Новости', contact: 'Контакты', cart: 'Корзина', orders: 'Мои заказы', login: 'Войти', register: 'Регистрация' },
    hero: {
      badge: '✨ Лучший ресторан Ташкента',
      title: 'Добро пожаловать в наш ресторан!',
      sub: 'Каждое блюдо готовится с любовью от чистого сердца. Почувствуйте уникальное сочетание национальных и мировых блюд у нас.',
      btn_menu: '🍽 Посмотреть меню',
      btn_about: 'О нас',
      stat_dishes: 'Блюд',
      stat_clients: 'Клиентов',
      stat_experience: 'Лет опыта'
    },
    home: {
      services_badge: 'Наши услуги',
      services_title: 'Что вас ожидает?',
      services_sub: 'Лучшие блюда, комфорт и незабываемые впечатления',
      about_title: 'История и традиции нашего ресторана',
      about_desc: 'Obsidian был основан в 2009 году и за пятнадцать лет стал одним из самых популярных ресторанов Ташкента.',
      menu_title: 'От национальных блюд до мировых деликатесов',
      menu_desc: 'Посмотрите наш ассортимент национальных блюд, турецкой кухни и напитков через наше меню.',
      news_title: 'Последние новости нашего ресторана',
      news_desc: 'Будьте в курсе новых блюд и специальных предложений.',
      contact_title: 'Мы находимся в центре Ташкента',
      contact_desc: 'Найдите нас в Юнусабадском районе, в удобном месте.',
      more: 'Подробнее →'
    },
    about: {
      hero_title: 'Obsidian - жемчужина Ташкента',
      hero_sub: 'С 2009 года мы обслуживаем вас с качеством, добротой и уникальными блюдами.',
      story_badge: 'Наша история',
      story_title: '15 лет традиций и любви',
      story_p1: 'Obsidian начал свою деятельность в 2009 году как небольшой семейный ресторан.',
      story_p2: 'Сегодня мы стали одним из самых популярных ресторанов Ташкента вместимостью более 500 гостей.',
      value_quality: 'Качество',
      value_quality_desc: 'Только самые свежие продукты',
      value_skill: 'Мастерство',
      value_skill_desc: 'Опытные повара',
      value_natural: 'Натуральность',
      value_natural_desc: 'Блюда без искусственных красителей',
      value_award: 'Признание',
      value_award_desc: 'Обладатель множества наград',
      stats_exp: 'Лет опыта',
      stats_dishes: 'Ассортимент блюд',
      stats_clients: 'Довольных клиентов',
      stats_chefs: 'Профессиональных поваров',
      cta_title: 'Посетите нас!',
      cta_sub: 'Открыто каждый день с 10:00 до 23:00. Свяжитесь с нами для бронирования.'
    },
    menu: {
      title: 'Меню Ресторана',
      sub: 'От национальных блюд до мировых деликатесов',
      sections: 'Разделы',
      open_hint: 'Нажмите, чтобы открыть →',
      close: 'Закрыть',
      select_cat: 'Выберите раздел слева',
      empty: 'В этом разделе пока нет блюд',
      page: 'стр.',
      add: 'В корзину',
      added: 'Добавлено'
    },
    cart: {
      title: 'Корзина',
      empty: 'Корзина пуста',
      total: 'Итого',
      checkout: 'Оформить заказ',
      type: 'Тип заказа',
      dining: 'В ресторане',
      delivery: 'Доставка',
      payment: 'Способ оплаты',
      cash: 'Наличные',
      card: 'Картой',
      time: 'Время доставки',
      asap: 'Как можно скорее',
      ai_title: 'AI рекомендует вам',
      address_label: 'Адрес доставки',
      address_select: 'Выберите адрес на карте',
      btn_order: 'Заказать'
    },
    footer: {
      pages: 'Страницы',
      contact: 'Контакты',
      address: 'Адрес',
      rights: 'Все права защищены.'
    },
    auth: { login: 'Войти', register: 'Регистрация', logout: 'Выйти' }
  },
  en: {
    nav: { home: 'Home', about: 'About', menu: 'Menu', news: 'News', contact: 'Contact', cart: 'Cart', orders: 'My Orders', login: 'Login', register: 'Register' },
    hero: {
      badge: '✨ Best restaurant in Tashkent',
      title: 'Welcome to Our Restaurant!',
      sub: 'Every dish is prepared with love from the heart. Feel the unique harmony of national and international dishes with us.',
      btn_menu: '🍽 View Menu',
      btn_about: 'About Us',
      stat_dishes: 'Dishes',
      stat_clients: 'Clients',
      stat_experience: 'Years Experience'
    },
    home: {
      services_badge: 'Our Services',
      services_title: 'What awaits you?',
      services_sub: 'Best food, comfort and unforgettable experience',
      about_title: 'History and traditions of our restaurant',
      about_desc: 'Obsidian was founded in 2009 and has become one of the most famous restaurants in Tashkent over fifteen years.',
      menu_title: 'From national dishes to international delicacies',
      menu_desc: 'See our wide range of national dishes, Turkish cuisine and drinks through our menu.',
      news_title: 'Latest news in our restaurant',
      news_desc: 'Stay tuned for new dishes and special offers.',
      contact_title: 'We are located in the center of Tashkent',
      contact_desc: 'Find us in Yunusabad district, in a convenient location.',
      more: 'Read more →'
    },
    about: {
      hero_title: 'Obsidian - the gem of Tashkent',
      hero_sub: 'Since 2009, we have been serving you with quality, kindness and unique dishes.',
      story_badge: 'Our Story',
      story_title: '15 years of tradition and love',
      story_p1: 'Obsidian started in 2009 as a small family restaurant.',
      story_p2: 'Today we have become one of the most famous restaurants in Tashkent with a capacity of over 500 guests.',
      value_quality: 'Quality',
      value_quality_desc: 'Only the freshest products',
      value_skill: 'Skill',
      value_skill_desc: 'Experienced chefs',
      value_natural: 'Naturalness',
      value_natural_desc: 'Dishes without artificial colors',
      value_award: 'Recognition',
      value_award_desc: 'Winner of many awards',
      stats_exp: 'Years experience',
      stats_dishes: 'Range of dishes',
      stats_clients: 'Happy clients',
      stats_chefs: 'Professional chefs',
      cta_title: 'Visit us!',
      cta_sub: 'Open every day from 10:00 to 23:00. Contact us for reservations.'
    },
    menu: {
      title: 'Restaurant Menu',
      sub: 'From national dishes to international delicacies',
      sections: 'Sections',
      open_hint: 'Click to open →',
      close: 'Close',
      select_cat: 'Select a section from the left',
      empty: 'No items in this section yet',
      page: 'page',
      add: 'Add',
      added: 'Added'
    },
    cart: {
      title: 'Cart',
      empty: 'Cart is empty',
      total: 'Total',
      checkout: 'Checkout',
      type: 'Order Type',
      dining: 'Dining',
      delivery: 'Delivery',
      payment: 'Payment Type',
      cash: 'Cash',
      card: 'By Card',
      time: 'Delivery Time',
      asap: 'As soon as possible',
      ai_title: 'AI recommends for you',
      address_label: 'Delivery Address',
      address_select: 'Select address on map',
      btn_order: 'Place Order'
    },
    footer: {
      pages: 'Pages',
      contact: 'Contact',
      address: 'Address',
      rights: 'All rights reserved.'
    },
    auth: { login: 'Login', register: 'Register', logout: 'Logout' }
  },
  de: {
    nav: { home: 'Startseite', about: 'Über uns', menu: 'Menü', news: 'Neuigkeiten', contact: 'Kontakt', cart: 'Warenkorb', orders: 'Bestellungen', login: 'Anmelden', register: 'Registrieren' },
    hero: {
      badge: '✨ Bestes Restaurant in Taschkent',
      title: 'Willkommen in unserem Restaurant!',
      sub: 'Jedes Gericht wird mit Liebe von Herzen zubereitet. Spüren Sie bei uns die einzigartige Harmonie nationaler und internationaler Gerichte.',
      btn_menu: '🍽 Menü ansehen',
      btn_about: 'Über uns',
      stat_dishes: 'Gerichte',
      stat_clients: 'Kunden',
      stat_experience: 'Jahre Erfahrung'
    },
    home: {
      services_badge: 'Unsere Leistungen',
      services_title: 'Was erwartet Sie?',
      services_sub: 'Bestes Essen, Komfort und unvergessliche Erlebnisse',
      about_title: 'Geschichte und Traditionen unseres Restaurants',
      about_desc: 'Obsidian wurde 2009 gegründet und hat sich in fünfzehn Jahren zu einem der bekanntesten Restaurants in Taschkent entwickelt.',
      menu_title: 'Von nationalen Gerichten bis hin zu internationalen Delikatessen',
      menu_desc: 'Sehen Sie sich unser breites Sortiment an nationalen Gerichten, türkischer Küche und Getränken in unserem Menü an.',
      news_title: 'Aktuelle Neuigkeiten in unserem Restaurant',
      news_desc: 'Bleiben Sie auf dem Laufenden über neue Gerichte und Sonderangebote.',
      contact_title: 'Wir befinden uns im Zentrum von Taschkent',
      contact_desc: 'Finden Sie uns im Bezirk Yunusabad, an einem günstigen Standort.',
      more: 'Mehr lesen →'
    },
    about: {
      hero_title: 'Obsidian - das Juwel von Taschkent',
      hero_sub: 'Seit 2009 bedienen wir Sie mit Qualität, Freundlichkeit und einzigartigen Gerichten.',
      story_badge: 'Unsere Geschichte',
      story_title: '15 Jahre Tradition und Liebe',
      story_p1: 'Obsidian begann 2009 als kleines Familienrestaurant.',
      story_p2: 'Heute sind wir zu einem der bekanntesten Restaurants in Taschkent mit einer Kapazität von über 500 Gästen geworden.',
      value_quality: 'Qualität',
      value_quality_desc: 'Nur die frischesten Produkte',
      value_skill: 'Geschick',
      value_skill_desc: 'Erfahrene Köche',
      value_natural: 'Natürlichkeit',
      value_natural_desc: 'Gerichte ohne künstliche Farbstoffe',
      value_award: 'Anerkennung',
      value_award_desc: 'Gewinner zahlreicher Auszeichnungen',
      stats_exp: 'Jahre Erfahrung',
      stats_dishes: 'Speisenauswahl',
      stats_clients: 'Zufriedene Kunden',
      stats_chefs: 'Professionelle Köche',
      cta_title: 'Besuchen Sie uns!',
      cta_sub: 'Täglich von 10:00 bis 23:00 Uhr geöffnet. Kontaktieren Sie uns für Reservierungen.'
    },
    menu: {
      title: 'Restaurant Menü',
      sub: 'Von nationalen Gerichten bis hin zu internationalen Delikatessen',
      sections: 'Bereiche',
      open_hint: 'Zum Öffnen klicken →',
      close: 'Schließen',
      select_cat: 'Wählen Sie einen Bereich links',
      empty: 'In diesem Bereich gibt es noch keine Gerichte',
      page: 'seite',
      add: 'Savat',
      added: 'Hinzugefügt'
    },
    cart: {
      title: 'Warenkorb',
      empty: 'Warenkorb ist leer',
      total: 'Gesamt',
      checkout: 'Zur Kasse',
      type: 'Bestellart',
      dining: 'Im Restaurant',
      delivery: 'Lieferung',
      payment: 'Zahlungsart',
      cash: 'Barzahlung',
      card: 'Mit Karte',
      time: 'Lieferzeit',
      asap: 'So bald wie möglich',
      ai_title: 'AI empfiehlt Ihnen',
      address_label: 'Lieferadresse',
      address_select: 'Adresse auf der Karte auswählen',
      btn_order: 'Bestellung aufgeben'
    },
    footer: {
      pages: 'Seiten',
      contact: 'Kontakt',
      address: 'Adresse',
      rights: 'Alle Rechte vorbehalten.'
    },
    auth: { login: 'Anmelden', register: 'Registrieren', logout: 'Abmelden' }
  }
}

const state = reactive({
  lang: localStorage.getItem('lang') || 'uz'
})

export const useI18n = () => {
  const t = (path) => {
    const keys = path.split('.')
    let result = translations[state.lang]
    for (const key of keys) {
      if (result) result = result[key]
    }
    return result || path
  }

  const setLang = (lang) => {
    state.lang = lang
    localStorage.setItem('lang', lang)
  }

  const currentLang = computed(() => state.lang)

  return { t, setLang, currentLang }
}
