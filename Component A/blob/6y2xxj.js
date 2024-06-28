function showDialog() { $.each(arguments, function(e, a) { $(a).show() }) }

function hideDialog() { $.each(arguments, function(e, a) { $(a).hide() }) }

function openNav() { document.getElementById("mySidenav").style.width = "80%", document.getElementById("mySidenav").classList.add("active"), $(".sidenav-wrapper").animate({ opacity: "1" }), $(".sidenav-wrapper").css("display", "block") }
$(document).ready(function() {
    $(".search-res-div").length > 0 && $(".search-res-div a").each(function() {
        var e = this.href.split("?");
        e[1].length > 3 ? this.href = "/wps/portal?" + e[1] : this.href = "/wps/portal"
    })
}), $(function() {
    var e = $(window),
        a = $(".dropdown");
    $("body");
    e.on("click.Bst", function(e) {
        if (0 == a.has(e.target).length && !a.is(e.target))
            for (var o = 0; o < $(".dropdown-menu").length; o++) $($(".dropdown-menu")[o]).css("display", "none")
    })
}), $(function() {
    $(".dropdown").click(function() {
        const e = this;
        let a = 0,
            o = 0;
        $(this).hasClass("notshow") && (o = 1), "block" === $($(e).children()[1]).css("display") && (a = 1), console.log($(".dropdown-menu").length);
        for (var t = 0; t < $(".dropdown-menu").length; t++) $($(".dropdown-menu")[t]).css("display", "none");
        a || o || $($(e).children()[1]).css("display", "block")
    }), $(window).scroll(function() {
        var e = $(".main-header");
        $(window).scrollTop() >= 100 ? e.addClass("sticky") : e.removeClass("sticky");
        for (var a = 0; a < $(".dropdown-menu").length; a++) $($(".dropdown-menu")[a]).css("display", "none")
    }), $(window).scroll(function() {
        var e = $(".master-header");
        $(window).scrollTop() >= 50 ? e.addClass("sticky") : e.removeClass("sticky")
    });
    var e = $("#testimonial-carousel");
    e.length && (e.owlCarousel({ items: 1, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3 }), $(".custom-owl-prev").click(function() { e.trigger("prev.owl.carousel") }), $(".custom-owl-next").click(function() { e.trigger("next.owl.carousel") }));
    var a = $(".hcl-software-product-carousel");
    a.length && a.owlCarousel({ items: 4, nav: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, autoplayHoverPause: !0, slideBy: 4, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, slideBy: 1, stagePadding: 20 }, 700: { items: 3, slideBy: 3 }, 998: { items: 4, slideBy: 4 } } });
    ! function() {
        function e() { $("#rowCardCarousel").length && $("#rowCardCarousel").owlCarousel({ center: !0, items: 2, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, autoplayHoverPause: !0, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, stagePadding: 20 }, 500: { items: 2 } } }) }
        $(window).width() < 992 ? e() : $("#rowCardCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 992 ? e() : ((a = $("#rowCardCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    }();
    var o = $("#unica_success_stories_corousol");
    o.length && (o.owlCarousel({ items: 1, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, autoplayHoverPause: !0 }), $(".success-stories-left-arrow").click(function() { o.trigger("prev.owl.carousel") }), $(".success-stories-right-arrow").click(function() { o.trigger("next.owl.carousel") }));
    (function() {
        function e() { $("#successStoriesCarousel").length && $("#successStoriesCarousel").owlCarousel({ center: !0, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, stagePadding: 20 }, 650: { items: 2 }, 992: { items: 3 } } }) }
        $(window).width() < 991 ? e() : $("#successStoriesCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 991 ? e() : ((a = $("#successStoriesCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    })(),
    function() {
        function e() { $("#appscansuccessStoriesCarousel").length && $("#appscansuccessStoriesCarousel").owlCarousel({ center: !0, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, stagePadding: 20 }, 650: { items: 2 }, 992: { items: 3 } } }) }
        $(window).width() < 991 ? e() : $("#appscansuccessStoriesCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 991 ? e() : ((a = $("#appscansuccessStoriesCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    }(),
    function() {
        function e() { $("#welcomeFaqCarousel").length && $("#welcomeFaqCarousel").owlCarousel({ center: !0, loop: !0, nav: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, responsiveClass: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1.2 }, 650: { items: 2 }, 992: { items: 3 } } }) }
        $(window).width() < 991 ? e() : $("#welcomeFaqCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 991 ? e() : ((a = $("#welcomeFaqCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    }();
    var t = $("#hclSoftwareCorousol");
    t.length && t.owlCarousel({ items: 1, stagePadding: 200, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], dots: !0, loop: !0, center: !0, responsive: { 0: { items: 1, stagePadding: 20 }, 992: { items: 1 } } });
    var l = $("#pnpVideoGallerySlider");
    l.length && l.owlCarousel({ items: 3, stagePadding: 200, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], dots: !0, loop: !0, center: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, autoplay: !0, autoPlaySpeed: 50, autoPlayTimeout: 50, responsive: { 0: { items: 1, stagePadding: 10 }, 768: { items: 1, stagePadding: 20 }, 1024: { items: 3, stagePadding: 20 } } });
    var s = $("#pnpdxVideoGallerySlider");
    s.length && s.owlCarousel({ items: 4, stagePadding: 200, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], dots: !0, loop: !0, center: !0, autoplay: !1, autoplayHoverPause: !1, autoplayTimeout: 4e3, autoplay: !1, autoPlaySpeed: 50, autoPlayTimeout: 50, responsive: { 0: { items: 1, stagePadding: 10 }, 768: { items: 1, stagePadding: 20 }, 1024: { items: 3, stagePadding: 20 } } });
    (function() {
        function e() { $("#pnpOurProductsCarousel").length && $("#pnpOurProductsCarousel").owlCarousel({ center: !0, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, stagePadding: 20 }, 650: { items: 2 }, 992: { items: 3 } } }) }
        $(window).width() < 991 ? e() : $("#pnpOurProductsCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 991 ? e() : ((a = $("#pnpOurProductsCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    })(),
    function() {
        function e() { $("#pnpOurProductsdevopsCarousel").length && $("#pnpOurProductsdevopsCarousel").owlCarousel({ center: !0, loop: !0, autoplay: !0, autoplayHoverPause: !0, autoplayTimeout: 4e3, nav: !0, navText: ['<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M20.829 32.774c.338.301.87.301 1.21 0a.7.7 0 0 0 0-1.064l-6.133-5.452h20.235c.472 0 .859-.333.859-.752 0-.42-.387-.764-.859-.764H15.906l6.132-5.441a.712.712 0 0 0 0-1.075.923.923 0 0 0-1.21 0l-7.583 6.742a.7.7 0 0 0 0 1.064l7.584 6.742z" /></svg>', '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50"> <path fill="#4B4B4B" fill-rule="nonzero" d="M29.171 32.774c-.338.301-.87.301-1.21 0a.7.7 0 0 1 0-1.064l6.133-5.452H13.859c-.472 0-.859-.333-.859-.752 0-.42.387-.764.859-.764h20.235l-6.132-5.441a.712.712 0 0 1 0-1.075c.338-.301.87-.301 1.21 0l7.583 6.742a.7.7 0 0 1 0 1.064l-7.584 6.742z" /> </svg>'], responsive: { 0: { items: 1, stagePadding: 20 }, 650: { items: 2 }, 992: { items: 3 } } }) }
        $(window).width() < 991 ? e() : $("#pnpOurProductsdevopsCarousel").addClass("off"), $(window).resize(function() {
            var a;
            $(window).width() < 991 ? e() : ((a = $("#pnpOurProductsdevopsCarousel")).trigger("destroy.owl.carousel"), a.addClass("off"))
        })
    }()
});
var siderDrawerEle = document.getElementById("drawer-overlay");

function openMasterNav() { document.getElementById("myPnpSidenav").classList.add("master-sidenav-open"), document.getElementById("myPnpSidenav").classList.add("master-mob-sidenav-open"), document.getElementById("myPnpSidenav").classList.add("active"), document.getElementById("drawer-overlay") && document.getElementById("drawer-overlay").classList.add("sidedrawer-wrapper"), document.getElementById("mainHeader") && document.getElementById("mainHeader").classList.add("header-zIndex"), $(".sidedrawer-wrapper").css("display", "block"), $(".sidedrawer-wrapper").animate({ opacity: "1" }) }

function closeNav() { document.getElementById("mySidenav").style.width = "0", document.getElementById("mySidenav").classList.remove("active"), document.getElementById("masterHeader").classList.remove("header-zIndex") }

function closeMasterNav() { document.getElementById("myPnpSidenav").classList.remove("master-sidenav-open"), document.getElementById("myPnpSidenav").classList.remove("master-mob-sidenav-open"), document.getElementById("myPnpSidenav").classList.remove("active"), document.getElementById("drawer-overlay") && document.getElementById("drawer-overlay").classList.remove("sidedrawer-wrapper"), document.getElementById("mainHeader") && document.getElementById("mainHeader").classList.remove("header-zIndex"), $(".sidedrawer-wrapper").animate({ opacity: "0" }), $(".sidedrawer-wrapper").css("display", "none") }
null !== siderDrawerEle && siderDrawerEle.addEventListener("click", function() { 1 === document.getElementById("drawer-overlay").classList.length && closeMasterNav() }, !1), $("#masterHeader .mobile-menu-btn").on("click", function() { $(".sidedrawer-wrapper").animate({ opacity: "1" }), $(".sidedrawer-wrapper").css("display", "block") }), $("#mySidenav").find(".icon_wrapper .closebtn").on("click", function() { $(".sidenav-wrapper").animate({ opacity: "0" }), $(".sidenav-wrapper").css("display", "none") }), $("#myPnpSidenav").find(".icon_wrapper .closebtn").on("click", function() { $(".sidenav-wrapper").animate({ opacity: "0" }), $(".sidenav-wrapper").css("display", "none"), document.getElementById("m_Products").classList.remove("in"), document.getElementById("m_hclSoft").classList.remove("in"), document.getElementById("m_Resources").classList.remove("in"), $(".accordian_wrapper").find("a").addClass("collapsed") }), $(".index_banner_learnmore").click(function() { $("html,body").animate({ scrollTop: $(".navigate-your-enterprize-section").offset().top - 100 }, "slow") }), jQuery(function(e) {
    var a = function() {
        var o = e(window).scrollTop() + e(window).height(),
            t = e(".animatable");
        0 == t.length && e(window).off("scroll", a), t.each(function(a) {
            var t = e(this);
            t.offset().top + t.height() - 200 < o && t.removeClass("animatable").addClass("animated")
        })
    };
    e(window).on("scroll", a), e(window).trigger("scroll")
});
$(document).ready(function() {
    $('h2').each(function() {
        if ($(this).text().trim() === '')
            $(this).remove();
    });
    $('h3').each(function() {
        if ($(this).text().trim() === '')
            $(this).remove();
    });
    $('h4').each(function() {
        if ($(this).text().trim() === '')
            $(this).remove();
    });
});

// $(document).ready(function() {
//     $(document).click(function(e) {
//         if (!$(e.target).is('.megamenu-body, .megamenu-body a, .megamenu-body div, .megamenu-body ul, .megamenu-body li, .megamenu-body h4, .megamenu-body h3, .megamenu-body h5')) {
//             $('.collapse').collapse('hide');
//         }
//     });

//     $('.toggle-btn').click(function(e) {
//         e.stopPropagation();
//         $('#navbar').fadeIn().toggleClass('is-open');
//         $(this).toggleClass('is-active');
//         $('body').toggleClass('no-scroll');
//     });
// });



$(document).ready(function() {
    $('.toggle-btn').click(function(e) {
        e.stopPropagation();
        $('.navbar-menu').fadeIn().toggleClass('open');
        $('.toggle-btn').toggleClass('active');
        $('body').toggleClass('no-scroll');
        $('.bg-overlay').toggleClass('active');
    });
    $('.bg-overlay').click(function() {
        $('body').removeClass('no-scroll');
        $('.toggle-btn, .bg-overlay').removeClass('active');
        $('.navbar-menu').removeClass('open');
    });
    $(".scroll_action").click(function(e) {
        var scroll_anchor = $(this).attr("href");
        //alert(scroll_anchor);
        $('html, body').animate({
            scrollTop: $(scroll_anchor).offset().top
        }, 800);
        e.preventDefault();
    });
});
