class Catalogue {
    constructor() {
        this.catalogue = [];
    }

    addBook(book) {
        this.catalogue.push(book);
    }

    getBook(index) {
        return this.catalogue[index];
    }

    getCatalogueSize() {
        return this.catalogue.length;
    }
}

module.exports = Catalogue;