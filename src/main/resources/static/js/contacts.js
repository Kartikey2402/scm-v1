console.log("contilf");
// const baseUrl = "http://localhost:8080";

var baseUrl;

async function fetchBaseUrl() {
    try {
        const response = await fetch('/api/config');
         baseUrl = await response.text();
        console.log("Base URL:", baseUrl);
    } catch (error) {
        console.error("Error fetching base URL:", error);
    }
}

fetchBaseUrl();


const viewContactModal = document.getElementById('view_contact_modal');

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        setTimeout(() => {
            contactModal.classList.add("scale-100");
        }, 50);
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

async function loadContactData(id) {
    // function call to load data
    console.log(id);
    try {
        const data = await (await fetch(`${baseUrl}/api/contacts/${id}`)).json();
        console.log(data);
        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.email;
        document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
        document.querySelector("#contact_image").src = data.picture;

        document.querySelector("#contact_address").innerHTML = data.address;
        document.querySelector("#contact_about").innerHTML = data.description;
        const contactFavourite = document.querySelector("#contact_favourite");
        if (data.favourite) {
            contactFavourite.innerHTML = "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";

        }
        else {
            contactFavourite.innerHTML = "Not Favourite Contact";
        }
        document.querySelector("#contact_website").href = data.websiteLink;
        document.querySelector("#contact_website").innerHTML = data.websiteLink;
        document.querySelector("#contact_linkedIn").href = data.linkedInLink;
        document.querySelector("#contact_linkedIn").innerHTML = data.websiteLink;
        openContactModal();
    } catch (error) {
        console.log("Error", error);
    }
}

async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to delete the contact",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        customClass: {
            popup: "bg-white  shadow-lg rounded-lg p-6",
            title: "text-lg font-semibold text-gray-800",
            confirmButton: "bg-red-500 hover:bg-red-600 text-white font-medium px-4 py-2 rounded",
            cancelButton: "bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium px-4 py-2 rounded",
        },
    }).then((result) => {

        if (result.isConfirmed) {
            const url = `${baseUrl}/user/contact/delete/` + id;
            console.log("deleting contact");
            window.location.replace(url);
        }
    });
}