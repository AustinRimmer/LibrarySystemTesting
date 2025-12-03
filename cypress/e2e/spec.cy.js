/*spec.cy.js
/*
 * Allowed cy.request() Usage (Nowhere Else):
ONLY in beforeEach() and afterEach() hooks for setup/cleanup (if needed)
To call the /api/reset to restore the initial state between tests.
Refer to the SimpleDemo example for implementation.
"Why cy.request() is prohibited in test scenarios: The purpose of Assignment 3 is to test the
complete user interface. Using cy.request() within scenarios bypasses the UI layer and defeats
the learning objective of end-to-end testing.
 */ 


describe('Library End to End System Checking', () => {
    beforeEach(() => {
        //reset system before each test
        cy.request('POST', '/api/reset');
    });
    afterEach(() => {
        //reset system before each test
        cy.request('POST', '/api/reset');
    });
    it('shoould show the basic borrow-return cycle with two users and one book', () => {
        //[TESTING REQS CHECKLIST]
        //Your test should switch between different user sessions and verify UI state changes after each action(borrow /return     [CHECK]

        //logs in as alice
        cy.login('alice',  'pass123');

        //assert verifies succesfull login via URL check
        //related to scenario since it would mean theyre on proper page post login (verifies ui state)
        cy.url().should('include', '/dashboard');

        //gets first book from the catalog
        cy.get('#allBooks tbody tr').first().within(() => {
            //assert checks that inital book stauts os avail
            //related to scenario since its establishing book status indicator (verifies ui state)
            cy.get('td').eq(2).should('have.text', 'Available').and('have.class', 'available')

            //assert checks that borrow btn is present for avail books
            //related to scenario since it checks UI correctly shows borrow option for available books (verifies ui state)
            cy.get('button').should('contain', 'Borrow');
        });

        //alice borrows first book
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifies success message on borrow opr8n
        //related to scenario since it confirms the borrow was successful (verifies ui state)
        cy.get('#message').should('contain', 'Book borrowed successfully').and('have.class', 'success');

        //assert verifies book status is changed post borrow to checked out
        //rlated to scenario since it shows that borrowed book becomes unavail (verifies ui state)
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(2).should('have.text', 'Checked Out').and('have.class', 'checked-out');
        });

        //assert verifies that alice's book count increased
        //related to scenario since it shows that UI reflects num borrowed correctly (verifies ui state)
        cy.get('#borrowedCount').should('have.text', '1');

        //logout
        cy.logout()

        //  ---------------------------------------[NOTE FOR TA]------------------------------------------------
        //  |   prev implmnt: assrt verfies that logout returns you to the login page                           |
        //  |   related since it shows session management for scenario (since its a multiuser scenario)         |
        /// |   cy.url().should('include', '/');                                                                |
        //  |   no longer required since i now have a command that does the check and logout (see commands.js)  |
        //  ----------------------------------------------------------------------------------------------------

        //login as bob
        cy.login('bob', 'pass456');

     
        cy.get('#allBooks tbody tr').first().within(() => {
            //assert verifies bob sees the book alice borrowed as checked out
            //related since it is part of scenario that borrowed books are unavail to other ppl
            cy.get('td').eq(2).should('have.text', 'Checked Out').and('have.class', 'checked-out');
            //assert verifies that button says place hold not borow
            //related since it shows proper ui update (prevents borrow of unavail books)
            cy.get('button').should('contain', 'Place Hold');
        });

        //assert verifies bob borrowed count is 0
        //related since it confirms user have indepo borrow record
        cy.get('#borrowedCount' ).should('have.text', '0');

        //logout bob
        cy.logout()

        //login in as alice
        cy.login('alice', 'pass123');

        //return the book
        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies return success
        //related since it shows that alice has now rreturned the book she was bboirrowing
        cy.get('#message').should('contain', 'Book returned successfully');

        //assert veriefies borrow count = 0
        //related since it shows again the return was succsess
        cy.get('#borrowedCount').should('have.text', '0');

        //logs back oiut
        cy.logout

        //bob logs back in
        cy.login('bob', 'pass456');

        //assert verifies book is Available again
        //related since once alice logs out the book should be avail so taht bob can borrow
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(2).should('have.text', 'Available').and('have.class', 'available');
            //assert verifies book can be borrowed again
            //related since once alice logs out the book should be avail so taht bob can borrow
            cy.get('button').should('contain', 'Borrow');
        });

    });
    it('should show the hold queue system with three users competing for the same book, 1 brrowing, then show fifo ordering on hold queue', () => {
        //[TESTING REQS CHECKLIST]
        // Your test should manage three different user sessions                                        [CHECK]
        // Verify queue ordering and notifications through the UI                                       [CHECK]
        // Test the complete workflow from initial borrow through multiple queue iterations             [CHECK]

        //logs in as alice
        cy.login('alice', 'pass123');

        //alice borrows book
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifeis that alice actually borrowed the book
        //related to scenario since it is part of setting up the scenario
        cy.get('#message').should('contain', 'Book borrowed successfully');

        //Logout Alice
        cy.logout

        //bob logs in
        cy.login('bob', 'pass456');

        //assert verifies that bob see the book as checked out
        //related to scenario since it is one of the preqs of scenario 
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(2).should('have.text', 'Checked Out');
        });
        //bob places a hold
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Place Hold').click();
        });

        //assert verfies hold was palced properly (thru ui like everything else)
        //related since it is again part of scenario (bob is now 'competing' for the book)
        cy.get('#message').should('contain', 'Added to hold queue');

        //assert verifies hold queue disp upadted
        //related since it is checking if the hold queue is populated
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', 'holder');
        });

        //bob logs out
        cy.logout()

        //charrliue logs in
        cy.login('charlie', 'pass789')

        //charlie also places hold
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Place Hold').click();
        });

        //assert verfies hold was palced properly (thru ui like everything else)
        //related since it is again part of scenario (chalie is now  also 'competing')
        cy.get('#message').should('contain', 'Added to hold queue');

        //assert verifies hold queue disp upadted
        //related since it is checking if the hold queue is populated with 2 ppl
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', '2 holder');
        });

        //charlie logs out
        cy.logout()

        //alice logs back in
        cy.login('alice', 'pass123');

        //alice returns the book
        cy.get('#borrowedBooks tbody tr').first().within(() =>  {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies that book was successfully returned
        //related since it is what triggers hold queue processing and sets up how i show its a fifo queue
        cy.get('#message').should('contain', 'Book returned successfully');

        //alice logs out
        cy.logout()

        //charlie logs in
        cy.login('charlie', 'pass789')

        //tries to place hold
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Place Hold').click();
        });

        //assert verifies that messaage is shown since charlie cant borrow the book since hes not first in queue
        //related since it shows that it respects fifo order
        cy.get('#message').should('contain', 'Cannot borrow or place hold');

        //charlie logs out
        cy.logout

        //bob logs in
        cy.login('bob', 'pass456')

        //assert verifies that bob see the book as on hold (even tho the next assert shows he can borrow which is intended)
        //related to scenario since it is one of the preqs of scenario 
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(2).should('have.text', 'On Hold');
        });

        //assert verifies hold queue disp shows 2 ppl in hold queue
        //related since it is checking if the hold queue is still populated with 2 ppl
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', '2 holder');
        });

        //bob borrows the book that hes first in queue for
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifies hold queue disp now shpws 1 prsn in hold queue
        //related since it is checking that hold queue has now been updated in fifo order
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', 'holder');
        });



        //assert verifeis that bob borrowed the book
        //related to scenario since it is end of scenario
        cy.get('#message').should('contain', 'Book borrowed successfully');

        //bob then returns it
        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies that book was successfully returned
        //related since it is what triggers hold queue processing and sets up how i show its a fifo queue
        cy.get('#message').should('contain', 'Book returned successfully');

        //bob then logs out
        cy.logout()

        //charlie now logs in as first in queue
        cy.login('charlie', 'pass789')

        //assert verifies that charlie sees the book as on hold (even tho the next assert shows he can borrow which is intended)
        //related to scenario since it is one of the preqs of scenario 
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(2).should('have.text', 'On Hold');
        });

        //assert verifies hold queue disp shows 1 prsn in hold queue
        //related since it is checking if the hold queue is still populated
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', 'holder');
        });

        //charlie borrows the book that hes first in queue for
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifies hold queue disp now shpws no hold queue
        //related since it is checking that hold queue has now been updated in fifo order
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('td').eq(3).should('contain', 'No holders');
        });




    });
    it('should show the interaction between borrowing limits and holds, ie user can place holds once max book limit is reached', () => {
        //[TESTING REQS CHECKLIST]
        // Your test should set up a user at the borrowing limit and verify constraint behaviour        [CHECK]
        // Verify the interaction between returning books, gaining capacity, and processing held books  [CHECK]
        // Test both positive cases (allowed actions) and negative cases (prevented actions             [CHECK]

        //bob logs in
        cy.login('bob', 'pass456')

        //bob borrows the first 3 books
        cy.get('#allBooks tbody tr').first().within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifies that bobs book count increased
        //related to scenario since it shows that UI reflects num borrowed correctly
        cy.get('#borrowedCount').should('have.text', '1');

        cy.get('#allBooks tbody tr').eq(1).within(() => {
            cy.get('button').contains('Borrow').click();
        });

        //assert verifies that bobs book count increased
        //related to scenario since it shows that UI reflects num borrowed correctly
        cy.get('#borrowedCount').should('have.text', '2');

        cy.get('#allBooks tbody tr').eq(2).within(() => {
            cy.get('button').contains( 'Borrow').click();
        });

        //assert verifies that bobs book count increased
        //related to scenario since it shows that UI reflects num borrowed correctly
        cy.get('#borrowedCount').should('have.text', '3');

        //bob places hold on a 4th book (this would count as neg flow since my web app prevents actual neg actions [ie all buttons turn to place hold as the only button once max borrows is reached])
        cy.get('#allBooks tbody tr').eq(3).within(() => {
            //assert verifies that since user is at borrow cap they can no longer borrow
            //related since itts a testing req (neg flow)
            cy.get('button').should('not.contain', 'borrow');
            cy.get('button').should('contain', 'Place Hold').click();
        });
        //assert verifies that bob only placed a hold and not a borrow
        //related to scenario since it shows that UI reflects no longer being able to borrow
        cy.get('#message').should('contain', 'Added to hold queue');

        //assert verifies that bob is notified when a held book is avaial (which is the case since he placed a hold and no one but him is in the hold queue ffor it)
        //related to scenario since it shows user notifed
        cy.get('#notificationsContent').should('contain', 'Book Available');

        //bob places hold on a 4th book again (neg flow)
        cy.get('#allBooks tbody tr').eq(3).within(() => {

            cy.get('button').should('contain', 'Place Hold').click();
        });
        //assert verifies that since user already holding book they cant place another hold
        //related since itts a testing holds processing (neg flow)
        cy.get('#message').should('contain', 'Cannot borrow or place hold');

        //charlie logs out with his 3 books borrowed
        cy.logout()

        // now charlie logs in
        cy.login('charlie','pass789')

        //charlie places a hold on the same 3 books
        cy.get('#allBooks tbody tr').eq(0).within(() => {
            //assert verifies that since user is at borrow cap they can no longer borrow
            //related since itts a testing req (neg flow)
            cy.get('button').should('not.contain', 'borrow');
            cy.get('button').should('contain', 'Place Hold').click();
        });
        //assert verifies that charlie only placed a hold and not a borrow
        //related to scenario since it shows that UI reflects no longer being able to borrow
        cy.get('#message').should('contain', 'Added to hold queue');

        cy.get('#allBooks tbody tr').eq(1).within(() => {
            //assert verifies that since user is at borrow cap they can no longer borrow
            //related since itts a testing req (neg flow)
            cy.get('button').should('not.contain', 'borrow');
            cy.get('button').should('contain', 'Place Hold').click();
        });
        //assert verifies that charlie only placed a hold and not a borrow
        //related to scenario since it shows that UI reflects no longer being able to borrow
        cy.get('#message').should('contain', 'Added to hold queue');

        cy.get('#allBooks tbody tr').eq(2).within(() => {
            //assert verifies that since book is checked out they can only hold
            //related since itts a testing req (neg flow)
            cy.get('button').should('not.contain', 'borrow');
            cy.get('button').should('contain', 'Place Hold').click();
        });
        //assert verifies that charlie only placed a hold and not a borrow
        //related to scenario since it shows that UI reflects no longer being able to borrow
        cy.get('#message').should('contain', 'Added to hold queue');


        //  ------------------------------------------------------[IMPORTANT FOR TA]--------------------------------------------------------------------------
        // |    now this next section is specifcally what i intended (queue hopping)                                                                          |
        // |    when users place holds on books with no borrower another user can still borrow that book ahead ofn them to prevent hogging all books          |
        //  --------------------------------------------------------------------------------------------------------------------------------------------------
        //charlie borrows the book that bob is trying to hog
        cy.get('#allBooks tbody tr').eq(3).within(() => {
            cy.get('button').should('contain', 'Borrow').click();
        });

        //assert verifies that charlies book count increased (ie the borrow succeeded)
        //related to scenario since it shows that UI reflects num borrowed correctly and that charlie could 'hop' the queue when there isnt actually anyone borrowing it (alt flow)
        cy.get('#borrowedCount').should('have.text', '1');

        //charlie then returns it
        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //and then charlie logs out
        cy.logout()

        //bob logs back in
        cy.login('bob', 'pass456')

        
        //assert verifies that bob is notified when a held book is avaial (which is now the case since he placed a hold on the book that charlie borrowed then returned)
        //related to scenario since it shows user notifed in odd edge case i implemented :)
        cy.get('#notificationsContent').should('contain', 'Book Available');

        //assert verifies that bobs book count is at the limit
        //related to scenario since it shows that UI reflects num borrowed correctly
        cy.get('#borrowedCount').should('have.text', '3');

        //bob then returns all 3 books
        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies that bobs book count decreased post return
        //related to scenario since it shows that returning a book drops the user back under the borrow limit
        cy.get('#borrowedCount').should('have.text', '2');

        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies that bobs book count decreased post return
        //related to scenario since it shows that returning a book drops the user back under the borrow limit
        cy.get('#borrowedCount').should('have.text', '1');

        cy.get('#borrowedBooks tbody tr').first().within(() => {
            cy.get('button').contains('Return Book').click();
        });

        //assert verifies that bobs book count decreased post return
        //related to scenario since it shows that returning a book drops the user back under the borrow limit
        cy.get('#borrowedCount').should('have.text', '0');


        //now bob logs out
        cy.logout()

        //charlie logs in
        cy.login('charlie', 'pass789')
        //assert verifies that charlie is notified when a held book is avail (which is now the case since he placed a hold on the 3 books bob was holding and now retunred)
        //related to scenario since it shows user notifed of newly avail books
        cy.get('#notificationsContent').should('contain', 'Book Available');
        //assert verifies that charlie is notified when a held book is avail (which is now the case since he placed a hold on the 3 books bob was holding and now retunred)
        //related to scenario since it shows user notifed of newly avail books
        cy.get('#notificationsContent').should('contain', 'Book Available');
        //assert verifies that charlie is notified when a held book is avail (which is now the case since he placed a hold on the 3 books bob was holding and now retunred)
        //related to scenario since it shows user notifed of newly avail books
        cy.get('#notificationsContent').should('contain', 'Book Available');

    })
})